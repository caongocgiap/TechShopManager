package com.techshop.server.core.authentication.service;

import com.techshop.server.core.authentication.model.request.LogOutRequest;
import com.techshop.server.core.authentication.model.request.RefreshTokenRequest;
import com.techshop.server.core.common.ResponseObject;

public interface AuthService {

    ResponseObject<?> getRefreshToken(RefreshTokenRequest request);

    ResponseObject<?> logout(LogOutRequest request);

}
