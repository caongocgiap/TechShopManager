package com.techshop.server.core.feature.admin.operationsystem.controller;

import com.techshop.server.core.feature.admin.operationsystem.model.request.ModifyOperationSystemRequest;
import com.techshop.server.core.feature.admin.operationsystem.model.request.OperationSystemPaginationRequest;
import com.techshop.server.core.feature.admin.operationsystem.service.OperationSystemService;
import com.techshop.server.infrastructure.constant.ApiConstant;
import com.techshop.server.util.Helper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstant.API_OPERATION_SYSTEM_PREFIX)
@RequiredArgsConstructor
@CrossOrigin("*")
public class OperationSystemController {

    private final OperationSystemService operationSystemService;

    @GetMapping
    public ResponseEntity<?> get(OperationSystemPaginationRequest request) {
        return Helper.createResponseEntity(operationSystemService.getPaginationOperationSystem(request));
    }

    @PostMapping
    public ResponseEntity<?> createOperationSystem(@Valid @RequestBody ModifyOperationSystemRequest modifyOperationSystemRequest) {
        return Helper.createResponseEntity(operationSystemService.createOperationSystem(modifyOperationSystemRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOperationSystem(
            @Valid @RequestBody ModifyOperationSystemRequest modifyOperationSystemRequest,
            @PathVariable String id
    ) {
        return Helper.createResponseEntity(operationSystemService.updateOperationSystem(modifyOperationSystemRequest, id));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateOperationSystemStatus(@PathVariable String id) {
        return Helper.createResponseEntity(operationSystemService.updateOperationSystemStatus(id));
    }

}
