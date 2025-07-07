package com.techshop.server.core.feature.admin.gpu.service;

import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.core.feature.admin.gpu.model.request.GpuPaginationRequest;
import com.techshop.server.core.feature.admin.gpu.model.request.ModifyGpuRequest;

public interface GpuService {

    ResponseObject getPaginationGpu(GpuPaginationRequest gpuPaginationRequest);

    ResponseObject createGpu(ModifyGpuRequest modifyGpuRequest);

    ResponseObject updateGpu(ModifyGpuRequest modifyGpuRequest, String idGpu);

    ResponseObject updateGpuStatus(String idGpu);

}
