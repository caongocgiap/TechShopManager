package com.techshop.server.core.feature.admin.operationsystem.service;

import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.core.feature.admin.operationsystem.model.request.ModifyOperationSystemRequest;
import com.techshop.server.core.feature.admin.operationsystem.model.request.OperationSystemPaginationRequest;

public interface OperationSystemService {

    ResponseObject getPaginationOperationSystem(OperationSystemPaginationRequest operationSystemPaginationRequest);

    ResponseObject createOperationSystem(ModifyOperationSystemRequest modifyOperationSystemRequest);

    ResponseObject updateOperationSystem(ModifyOperationSystemRequest modifyOperationSystemRequest, String idOperationSystem);

    ResponseObject updateOperationSystemStatus(String idOperationSystem);

}
