package com.techshop.server.core.feature.admin.screenresolution.controller;

import com.techshop.server.core.feature.admin.screenresolution.model.request.ModifyScreenResolutionRequest;
import com.techshop.server.core.feature.admin.screenresolution.model.request.ScreenResolutionPaginationRequest;
import com.techshop.server.core.feature.admin.screenresolution.service.ScreenResolutionService;
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
@RequestMapping(ApiConstant.API_SCREEN_RESOLUTION_PREFIX)
@RequiredArgsConstructor
@CrossOrigin("*")
public class ScreenResolutionController {

    private final ScreenResolutionService screenResolutionService;

    @GetMapping
    public ResponseEntity<?> get(ScreenResolutionPaginationRequest request) {
        return Helper.createResponseEntity(screenResolutionService.getPaginationScreenResolution(request));
    }

    @PostMapping
    public ResponseEntity<?> createScreenResolution(@Valid @RequestBody ModifyScreenResolutionRequest modifyScreenResolutionRequest) {
        return Helper.createResponseEntity(screenResolutionService.createScreenResolution(modifyScreenResolutionRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateScreenResolution(
            @Valid @RequestBody ModifyScreenResolutionRequest modifyScreenResolutionRequest,
            @PathVariable String id
    ) {
        return Helper.createResponseEntity(screenResolutionService.updateScreenResolution(modifyScreenResolutionRequest, id));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateScreenResolutionStatus(@PathVariable String id) {
        return Helper.createResponseEntity(screenResolutionService.updateScreenResolutionStatus(id));
    }

}
