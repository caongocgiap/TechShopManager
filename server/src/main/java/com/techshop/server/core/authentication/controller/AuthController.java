package com.techshop.server.core.authentication.controller;

import com.techshop.server.core.authentication.model.request.LogOutRequest;
import com.techshop.server.core.authentication.model.request.RefreshTokenRequest;
import com.techshop.server.core.authentication.service.AuthService;
import com.techshop.server.infrastructure.constant.ApiConstant;
import com.techshop.server.util.Helper;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController is a REST controller that handles authentication related requests.
 * It is annotated with @RestController, indicating that it's a controller where every method returns a domain object instead of a view.
 * It's also annotated with @RequiredArgsConstructor, which generates a constructor with required fields.
 *
 * @CrossOrigin allows cross-origin requests.
 * @Slf4j provides a logger for the class.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstant.API_AUTH_PREFIX)
@CrossOrigin("*")
@Slf4j
@Hidden
public class AuthController {

    // AuthService is automatically injected into this controller using the constructor.
    private final AuthService authService;

    /**
     * Handles the POST request for refreshing the authentication token.
     *
     * @param request The request body containing the refresh token.
     * @return A ResponseEntity that includes the result of the refresh token operation.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return Helper.createResponseEntity(authService.getRefreshToken(request));
    }

    /**
     * Handles the POST request for logging out.
     *
     * @param request The request body containing the logout information.
     * @return A ResponseEntity that includes the result of the logout operation.
     */
    @PostMapping("/log-out")
    public ResponseEntity<?> logout(@RequestBody LogOutRequest request) {
        return Helper.createResponseEntity(authService.logout(request));
    }

}