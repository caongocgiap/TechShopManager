package com.techshop.server.core.feature.admin.screenresolution.model.response;

import com.techshop.server.core.common.HasOrderNumber;
import com.techshop.server.core.common.IsIdentify;

public interface ScreenResolutionResponse extends IsIdentify, HasOrderNumber {

    String getScreenResolutionId();

    String getScreenResolutionCode();

    String getScreenResolutionName();

    Long getScreenResolutionWidth();

    Long getScreenResolutionHeight();

    String getScreenResolutionAspectRatio();

    String getScreenResolutionStatus();

}
