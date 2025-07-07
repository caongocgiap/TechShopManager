package com.techshop.server.core.feature.admin.manufacturer.controller;

import com.techshop.server.core.feature.admin.manufacturer.model.request.ManufacturerPaginationRequest;
import com.techshop.server.core.feature.admin.manufacturer.model.request.ModifyManufacturerRequest;
import com.techshop.server.core.feature.admin.manufacturer.service.ManufacturerService;
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
@RequestMapping(ApiConstant.API_MANUFACTURER_PREFIX)
@RequiredArgsConstructor
@CrossOrigin("*")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @GetMapping
    public ResponseEntity<?> get(ManufacturerPaginationRequest request) {
        return Helper.createResponseEntity(manufacturerService.getPaginationManufacturer(request));
    }

    @PostMapping
    public ResponseEntity<?> createManufacturer(@Valid @RequestBody ModifyManufacturerRequest modifyManufacturerRequest) {
        return Helper.createResponseEntity(manufacturerService.createManufacturer(modifyManufacturerRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateManufacturer(
            @Valid @RequestBody ModifyManufacturerRequest modifyManufacturerRequest,
            @PathVariable String id
    ) {
        return Helper.createResponseEntity(manufacturerService.updateManufacturer(modifyManufacturerRequest, id));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateManufacturerStatus(@PathVariable String id) {
        return Helper.createResponseEntity(manufacturerService.updateManufacturerStatus(id));
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return Helper.createResponseEntity(manufacturerService.getAll());
    }

}
