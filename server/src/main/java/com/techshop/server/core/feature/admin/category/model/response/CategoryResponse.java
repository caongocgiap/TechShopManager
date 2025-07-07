package com.techshop.server.core.feature.admin.category.model.response;

import com.techshop.server.core.common.HasOrderNumber;
import com.techshop.server.core.common.IsIdentify;

public interface CategoryResponse extends IsIdentify, HasOrderNumber {

    String getCategoryId();

    String getCategoryCode();

    String getCategoryName();

    String getCategoryStatus();

}
