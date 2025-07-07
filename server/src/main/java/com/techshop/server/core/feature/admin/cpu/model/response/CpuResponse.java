package com.techshop.server.core.feature.admin.cpu.model.response;

import com.techshop.server.core.common.HasOrderNumber;
import com.techshop.server.core.common.IsIdentify;

public interface CpuResponse extends IsIdentify, HasOrderNumber {

    String getCpuId();

    String getCpuModel();

    String getCpuManufacturerName();

    String getCpuSeries();

    String getCpuGeneration();

    Long getCpuCores();

    Long getCpuThreads();

    double getCpuBaseClock();

    double getCpuTurboClock();

    Long getCpuCacheSize();

    Long getCpuTdpWatt();

    String getCpuArchitecture();

    Long getCpuReleaseYear();

    String getCpuStatus();

}
