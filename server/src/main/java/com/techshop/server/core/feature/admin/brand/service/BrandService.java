package com.techshop.server.core.feature.admin.brand.service;

import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.core.feature.admin.brand.model.request.BrandPaginationRequest;
import com.techshop.server.core.feature.admin.brand.model.request.ModifyBrandRequest;

public interface BrandService {

    ResponseObject getPaginationBrands(BrandPaginationRequest brandPaginationRequest);

    ResponseObject createBrand(ModifyBrandRequest modifyBrandRequest);

    ResponseObject updateBrand(ModifyBrandRequest modifyBrandRequest, String idBrand);

    ResponseObject updateBrandStatus(String idBrand);

}
