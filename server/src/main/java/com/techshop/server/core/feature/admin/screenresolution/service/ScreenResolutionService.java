package com.techshop.server.core.feature.admin.screenresolution.service;

import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.core.feature.admin.screenresolution.model.request.ModifyScreenResolutionRequest;
import com.techshop.server.core.feature.admin.screenresolution.model.request.ScreenResolutionPaginationRequest;

public interface ScreenResolutionService {

    ResponseObject getPaginationScreenResolution(ScreenResolutionPaginationRequest screenResolutionPaginationRequest);

    ResponseObject createScreenResolution(ModifyScreenResolutionRequest modifyScreenResolutionRequest);

    ResponseObject updateScreenResolution(ModifyScreenResolutionRequest modifyScreenResolutionRequest, String idScreenResolution);

    ResponseObject updateScreenResolutionStatus(String idScreenResolution);

}
