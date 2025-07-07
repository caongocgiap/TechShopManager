package com.techshop.server.core.feature.admin.manufacturer.service;

import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.core.feature.admin.manufacturer.model.request.ManufacturerPaginationRequest;
import com.techshop.server.core.feature.admin.manufacturer.model.request.ModifyManufacturerRequest;

public interface ManufacturerService {

    ResponseObject getAll();

    ResponseObject getPaginationManufacturer(ManufacturerPaginationRequest manufacturerPaginationRequest);

    ResponseObject createManufacturer(ModifyManufacturerRequest modifyManufacturerRequest);

    ResponseObject updateManufacturer(ModifyManufacturerRequest modifyManufacturerRequest, String idManufacturer);

    ResponseObject updateManufacturerStatus(String idManufacturer);

}
