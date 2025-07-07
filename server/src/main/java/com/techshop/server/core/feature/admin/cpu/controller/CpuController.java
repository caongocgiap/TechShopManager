package com.techshop.server.core.feature.admin.cpu.controller;

import com.techshop.server.core.feature.admin.cpu.model.request.CpuPaginationRequest;
import com.techshop.server.core.feature.admin.cpu.model.request.ModifyCpuRequest;
import com.techshop.server.core.feature.admin.cpu.service.CpuService;
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
@RequestMapping(ApiConstant.API_CPU_PREFIX)
@RequiredArgsConstructor
@CrossOrigin("*")
public class CpuController {

    private final CpuService cpuService;

    @GetMapping
    public ResponseEntity<?> get(CpuPaginationRequest request) {
        return Helper.createResponseEntity(cpuService.getPaginationCpu(request));
    }

    @PostMapping
    public ResponseEntity<?> createCpu(@Valid @RequestBody ModifyCpuRequest modifyCpuRequest) {
        return Helper.createResponseEntity(cpuService.createCpu(modifyCpuRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCpu(
            @Valid @RequestBody ModifyCpuRequest modifyCpuRequest,
            @PathVariable String id
    ) {
        return Helper.createResponseEntity(cpuService.updateCpu(modifyCpuRequest, id));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateCpuStatus(@PathVariable String id) {
        return Helper.createResponseEntity(cpuService.updateCpuStatus(id));
    }

}
