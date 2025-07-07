package com.techshop.server.infrastructure.security.oauth2;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.techshop.server.entity.Client;
import com.techshop.server.infrastructure.constant.EntityStatus;
import com.techshop.server.infrastructure.constant.UserType;
import com.techshop.server.infrastructure.security.exception.RedirectException;
import com.techshop.server.infrastructure.security.repository.ClientAuthRepository;
import com.techshop.server.infrastructure.security.repository.StaffRoleAuthRepository;
import com.techshop.server.infrastructure.security.response.TokenUriResponse;
import com.techshop.server.infrastructure.security.service.RefreshTokenService;
import com.techshop.server.infrastructure.security.service.TokenProvider;
import com.techshop.server.util.CookieUtils;
import com.techshop.server.util.GenerateClientUtils;
import com.techshop.server.util.URLBuilderUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Optional;

import static com.techshop.server.infrastructure.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    private final RefreshTokenService refreshTokenService;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final ClientAuthRepository clientAuthRepository;

    @Value("${app.default-target-url}")
    private String DEFAULT_TARGET_URL;

    @Value("${app.default-target-url-staff}")
    private String DEFAULT_TARGET_URL_STAFF;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(
            TokenProvider tokenProvider,
            HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
            RefreshTokenService refreshTokenService,
            ClientAuthRepository clientAuthRepository,
            StaffRoleAuthRepository staffRoleAuthRepository
    ) {
        this.tokenProvider = tokenProvider;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.refreshTokenService = refreshTokenService;
        this.clientAuthRepository = clientAuthRepository;
    }

    //SET DEFAULT SECRET KEY
//    @PostConstruct
//    public void init() {
//        tokenProvider.setTokenSecret(getSecretKey());
//    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                    .map(Cookie::getValue);
            if (redirectUri.isEmpty()) throw new RedirectException("Redirect uri not found! Please try again later!");
            System.out.println("redirect uri: " + redirectUri.get());
            Optional<Client> client = processClient();
            if (client.isEmpty()) throw new RedirectException("Server error! Please try again later!");
            System.out.println("client get secret key: " + client.get().getSecretKey());
            tokenProvider.setTokenSecret(client.get().getSecretKey());
            String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
            String[] token = tokenProvider.createToken(authentication, DEFAULT_TARGET_URL);
            String tokenAuthorization = token[0];
            String userType = token[1];
            String status = token[2];

            if (status.equals(EntityStatus.DELETED.name()))
                throw new RedirectException("User has been deleted or inactive! Please try again later!");

            String refreshToken = refreshTokenService.createRefreshToken(authentication).getRefreshToken();
            if (tokenAuthorization.isEmpty() || refreshToken.isEmpty()) {
                throw new RedirectException("Server error! Please try again later!");
            }

//            tokenProvider.setTokenSecret(getSecretKey());

//            if (userType.equals(UserType.MEMBER.name()))
//                return buildSuccessUrl(
//                        DEFAULT_TARGET_URL_STAFF,
//                        new TokenUriResponse(tokenAuthorization, refreshToken).getTokenAuthorizationSimple()
//                );
//            else
                return buildSuccessUrl(
                        targetUrl,
                        new TokenUriResponse(tokenAuthorization, refreshToken).getTokenAuthorizationSimple()
                );
        } catch (
                BadRequestException |
                JsonProcessingException |
                MalformedURLException |
                URISyntaxException |
                RedirectException e
        ) {
            e.printStackTrace(System.out);
            return buildErrorUri(e.getMessage());
        }
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private Optional<Client> processClient() {
        String clientSecret = GenerateClientUtils.generateRandomClientSecret(32);
        String clientId = GenerateClientUtils.generateRandomClientID();
        Client newClient = new Client();
        newClient.setClientId(clientId);
        newClient.setClientSecret(clientSecret);
        newClient.setEntityStatus(EntityStatus.NOT_DELETED);
        Client clientSaved = clientAuthRepository.save(newClient);
        if (clientSaved.getSecretKey() == null) {
            SecretKey tokenSecret = GenerateClientUtils.generateJwtSecretKey(clientSaved.getClientId(), clientSaved.getClientSecret());
            clientSaved.setSecretKey(tokenSecret);
            clientAuthRepository.save(clientSaved);
        }
        return Optional.of(clientSaved);
    }

    private SecretKey getSecretKey() {
        String clientId = GenerateClientUtils.generateRandomClientID();
        String clientSecret = GenerateClientUtils.generateRandomClientSecret(32);
        SecretKey secretKey = GenerateClientUtils.generateJwtSecretKey(clientId, clientSecret);
        Client newClient = new Client();
        newClient.setClientId(clientId);
        newClient.setClientSecret(clientSecret);
        newClient.setSecretKey(secretKey);
        Client clientSaved = clientAuthRepository.save(newClient);
        return clientSaved.getSecretKey();
    }

    private String buildErrorUri(String errorMessage) {
        return DEFAULT_TARGET_URL + "/error?error=" + errorMessage;
    }

    private String buildSuccessUrl(String targetUrl, String token) throws MalformedURLException, URISyntaxException {
        URLBuilderUtil urlBuilder = new URLBuilderUtil(targetUrl);
        urlBuilder.addParameter("state", token);
        try {
            String relativeURL = urlBuilder.getRelativeURL();
            return targetUrl + relativeURL;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return DEFAULT_TARGET_URL + "/error?error=Server error! Please try again later!";
        }
    }

}
