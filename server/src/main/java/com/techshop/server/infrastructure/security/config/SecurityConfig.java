package com.techshop.server.infrastructure.security.config;

import com.techshop.server.infrastructure.constant.ApiConstant;
import com.techshop.server.infrastructure.constant.RoleConstants;
import com.techshop.server.infrastructure.security.exception.RestAuthenticationEntryPoint;
import com.techshop.server.infrastructure.security.filter.TokenAuthenticationFilter;
import com.techshop.server.infrastructure.security.oauth2.CustomOAuth2UserService;
import com.techshop.server.infrastructure.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.techshop.server.infrastructure.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.techshop.server.infrastructure.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.techshop.server.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Value("${frontend.url}")
    private List<String> allowedOrigins;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        source.registerCorsConfiguration("/**", config.applyPermitDefaultValues());
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "*"));
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));
        return source;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(c -> c.configurationSource(corsConfigurationSource()));
        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.exceptionHandling(e -> e.authenticationEntryPoint(new RestAuthenticationEntryPoint()));
        http.authorizeHttpRequests(auth -> auth.requestMatchers(
                        "/",
                        "/error",
                        "/favicon.ico",
                        "/*/*.png",
                        "/*/*.gif",
                        "/*/*.svg",
                        "/*/*.jpg",
                        "/*/*.html",
                        "/*/*.css",
                        "/*/*.js"
                )
                .permitAll());
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                                "/auth/**",
                                Helper.appendWildcard(ApiConstant.API_AUTH_PREFIX),
                                Helper.appendWildcard(ApiConstant.API_CONNECTOR_PREFIX),
                                "/oauth2/**",
                                "/api/entry-module/**",
                                "/api/support/mail/**",
                                "/api/module-switch/**"
                        )
                        .permitAll()
        );
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                                Helper.appendWildcard(ApiConstant.API_SWAGGER_API_PREFIX),
                                Helper.appendWildcard(ApiConstant.API_SWAGGER_UI_PREFIX)
                        )
                        .permitAll()
        );
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                        Helper.appendWildcard(ApiConstant.API_ADMIN_PREFIX),
                        Helper.appendWildcard(ApiConstant.API_BRAND_PREFIX)
                        )
                        .hasAnyAuthority(RoleConstants.ACTOR_ADMIN));
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                                Helper.appendWildcard(ApiConstant.API_USER_PREFIX)
                        )
                        .hasAnyAuthority(RoleConstants.ACTOR_ADMIN, RoleConstants.ACTOR_MEMBER)
        );

        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                                ApiConstant.API_ADMIN_PREFIX + "/update-staff/{id}",
                                ApiConstant.API_ADMIN_PREFIX + "/update-status-staff/{id}",
                                ApiConstant.API_ADMIN_PREFIX + "/detail-staff/{id}"
                        )
                        .hasAnyAuthority(String.valueOf(RoleConstants.defaultRolesForStaff))
        );

        //disable security for websocket
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                                "/ws-message/**",
                                "/send/**",
                                "/sendMessage/**",
                                "/topic/message/**"
                        )
                        .permitAll()
        );
        http.oauth2Login(oauth2 -> oauth2.authorizationEndpoint(a -> a.baseUri("/oauth2/authorize"))
                .redirectionEndpoint(r -> r.baseUri("/oauth2/callback/**"))
                .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                .authorizationEndpoint(a -> a.authorizationRequestRepository(cookieAuthorizationRequestRepository()))
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler));
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
