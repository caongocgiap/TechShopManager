package com.techshop.server.core.feature.admin.brand.model.response;

import com.techshop.server.core.common.HasOrderNumber;
import com.techshop.server.core.common.IsIdentify;

public interface BrandResponse extends IsIdentify, HasOrderNumber {

    String getBrandId();

    String getBrandCode();

    String getBrandName();

    String getBrandStatus();

}
