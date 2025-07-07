package com.techshop.server.core.feature.admin.manufacturer.model.response;

import com.techshop.server.core.common.HasOrderNumber;
import com.techshop.server.core.common.IsIdentify;

public interface ManufacturerResponse extends IsIdentify, HasOrderNumber {

    String getManufacturerId();

    String getManufacturerCode();

    String getManufacturerName();

    String getManufacturerCountry();

    String getManufacturerWebsite();

    String getManufacturerDescription();

    String getManufacturerStatus();

}
