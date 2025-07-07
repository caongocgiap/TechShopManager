package com.techshop.server.core.authentication.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token is required")
    @NotNull(message = "Refresh token is required")
    private String refreshToken;

    @NotBlank(message = "Current host is required")
    @NotNull(message = "Current host is required")
    private String currentHost;

}
