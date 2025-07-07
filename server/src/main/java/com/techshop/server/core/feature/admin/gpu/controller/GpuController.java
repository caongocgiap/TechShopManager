package com.techshop.server.core.feature.admin.gpu.controller;

import com.techshop.server.core.feature.admin.gpu.model.request.GpuPaginationRequest;
import com.techshop.server.core.feature.admin.gpu.model.request.ModifyGpuRequest;
import com.techshop.server.core.feature.admin.gpu.service.GpuService;
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
@RequestMapping(ApiConstant.API_GPU_PREFIX)
@RequiredArgsConstructor
@CrossOrigin("*")
public class GpuController {

    private final GpuService gpuService;

    @GetMapping
    public ResponseEntity<?> get(GpuPaginationRequest request) {
        return Helper.createResponseEntity(gpuService.getPaginationGpu(request));
    }

    @PostMapping
    public ResponseEntity<?> createGpu(@Valid @RequestBody ModifyGpuRequest modifyGpuRequest) {
        return Helper.createResponseEntity(gpuService.createGpu(modifyGpuRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGpu(
            @Valid @RequestBody ModifyGpuRequest modifyGpuRequest,
            @PathVariable String id
    ) {
        return Helper.createResponseEntity(gpuService.updateGpu(modifyGpuRequest, id));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateGpuStatus(@PathVariable String id) {
        return Helper.createResponseEntity(gpuService.updateGpuStatus(id));
    }

}
