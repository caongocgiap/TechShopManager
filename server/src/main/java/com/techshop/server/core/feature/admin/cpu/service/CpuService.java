package com.techshop.server.core.feature.admin.cpu.service;

import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.core.feature.admin.cpu.model.request.CpuPaginationRequest;
import com.techshop.server.core.feature.admin.cpu.model.request.ModifyCpuRequest;

public interface CpuService {

    ResponseObject<?> getPaginationCpu(CpuPaginationRequest cpuPaginationRequest);

    ResponseObject<?> createCpu(ModifyCpuRequest modifyCpuRequest);

    ResponseObject<?> updateCpu(ModifyCpuRequest modifyCpuRequest, String idCpu);

    ResponseObject<?> updateCpuStatus(String idCpu);

}
