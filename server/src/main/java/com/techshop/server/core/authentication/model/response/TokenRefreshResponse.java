package com.techshop.server.core.authentication.model.response;

public record TokenRefreshResponse(String accessToken, String refreshToken) {
}
