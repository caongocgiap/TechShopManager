package com.techshop.server.core.feature.admin.brand.controller;

import com.techshop.server.core.feature.admin.brand.model.request.BrandPaginationRequest;
import com.techshop.server.core.feature.admin.brand.model.request.ModifyBrandRequest;
import com.techshop.server.core.feature.admin.brand.service.BrandService;
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
@RequestMapping(ApiConstant.API_BRAND_PREFIX)
@RequiredArgsConstructor
@CrossOrigin("*")
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<?> get(BrandPaginationRequest request) {
        return Helper.createResponseEntity(brandService.getPaginationBrands(request));
    }

    @PostMapping
    public ResponseEntity<?> createBrand(@Valid @RequestBody ModifyBrandRequest modifyBrandRequest) {
        return Helper.createResponseEntity(brandService.createBrand(modifyBrandRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(
            @Valid @RequestBody ModifyBrandRequest modifyBrandRequest,
            @PathVariable String id
    ) {
        return Helper.createResponseEntity(brandService.updateBrand(modifyBrandRequest, id));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateBrandStatus(@PathVariable String id) {
        return Helper.createResponseEntity(brandService.updateBrandStatus(id));
    }

}
