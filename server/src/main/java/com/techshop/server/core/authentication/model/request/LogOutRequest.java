package com.techshop.server.core.authentication.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogOutRequest {

    @NotNull
    private String userId;

    @NotNull
    private String userType;

}
