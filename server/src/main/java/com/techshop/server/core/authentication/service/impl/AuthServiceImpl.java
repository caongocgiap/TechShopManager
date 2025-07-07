package com.techshop.server.core.authentication.service.impl;

import com.techshop.server.core.authentication.model.request.LogOutRequest;
import com.techshop.server.core.authentication.model.request.RefreshTokenRequest;
import com.techshop.server.core.authentication.model.response.TokenRefreshResponse;
import com.techshop.server.core.authentication.repository.AccessTokenAuthEntryRepository;
import com.techshop.server.core.authentication.repository.RefreshTokenAuthEntryRepository;
import com.techshop.server.core.authentication.service.AuthService;
import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.entity.RefreshToken;
import com.techshop.server.infrastructure.constant.UserType;
import com.techshop.server.infrastructure.security.service.RefreshTokenService;
import com.techshop.server.infrastructure.security.service.TokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccessTokenAuthEntryRepository accessTokenAuthEntryRepository;

    private final RefreshTokenAuthEntryRepository refreshTokenAuthEntryRepository;

    private final RefreshTokenService refreshTokenService;

    private final TokenProvider tokenProvider;

    @Value("${app.default-target-url}")
    private String DEFAULT_TARGET_URL;

    @Override
    public ResponseObject<?> getRefreshToken(RefreshTokenRequest request) {
        try {
            String token = request.getRefreshToken();
            String currentHost = request.getCurrentHost();

            Optional<RefreshToken> refreshTokenOptional = refreshTokenAuthEntryRepository.findByRefreshToken(token);
            if (refreshTokenOptional.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Token not found");
            }

            RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenOptional.get());
            if (refreshToken == null) return new ResponseObject<>(
                    null,
                    HttpStatus.NOT_ACCEPTABLE,
                    "Token is expired"
            );

            String newAccessToken = tokenProvider.createToken(
                    refreshTokenOptional.get().getUserId(),
                    DEFAULT_TARGET_URL,
                    refreshTokenOptional.get().getUserType().toString()
            );
            return new ResponseObject<>(
                    new TokenRefreshResponse(
                            newAccessToken,
                            refreshTokenOptional.get().getRefreshToken()
                    ),
                    HttpStatus.OK,
                    "Token refreshed");
        } catch (Exception e) {
            return ResponseObject.errorForward(
                    "Error while refreshing token",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Transactional
    @Override
    public ResponseObject<?> logout(LogOutRequest request) {
        String userId = request.getUserId();
        UserType userType = UserType.valueOf(request.getUserType());
        refreshTokenAuthEntryRepository.deleteByUserId(userId, userType);
        accessTokenAuthEntryRepository.deleteByUserId(userId, userType);
        return new ResponseObject<>(null, HttpStatus.OK, "Logout success");
    }

}
