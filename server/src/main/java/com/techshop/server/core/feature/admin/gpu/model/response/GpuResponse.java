package com.techshop.server.core.feature.admin.gpu.model.response;

import com.techshop.server.core.common.HasOrderNumber;
import com.techshop.server.core.common.IsIdentify;

public interface GpuResponse extends IsIdentify, HasOrderNumber {

    String getGpuId();

    String getGpuModel();

    String getGpuManufacturer();

    String getGpuSeries();

    Long getGpuMemorySize();

    String getGpuMemoryType();

    boolean isGpuIntegrated();

    String getGpuStatus();

}
