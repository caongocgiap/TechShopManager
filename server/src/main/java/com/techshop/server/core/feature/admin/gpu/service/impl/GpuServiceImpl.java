package com.techshop.server.core.feature.admin.gpu.service.impl;

import com.techshop.server.core.common.PageableObject;
import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.core.feature.admin.gpu.model.request.GpuPaginationRequest;
import com.techshop.server.core.feature.admin.gpu.model.request.ModifyGpuRequest;
import com.techshop.server.core.feature.admin.gpu.repository.GpuExtendRepository;
import com.techshop.server.core.feature.admin.gpu.service.GpuService;
import com.techshop.server.entity.Gpu;
import com.techshop.server.infrastructure.constant.EntityStatus;
import com.techshop.server.infrastructure.constant.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GpuServiceImpl implements GpuService {

    private final GpuExtendRepository gpuExtendRepository;

    @Override
    public ResponseObject getPaginationGpu(GpuPaginationRequest request) {
        try {
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
            if (request.getSearchValues() == null) {
                return new ResponseObject<>(
                        PageableObject.of(gpuExtendRepository.search(pageable)),
                        HttpStatus.OK,
                        ResponseMessage.SUCCESS.getMessage()
                );
            }
            return new ResponseObject<>(
                    PageableObject.of(gpuExtendRepository.search(pageable, request)),
                    HttpStatus.OK,
                    ResponseMessage.SUCCESS.getMessage()
            );
        } catch (Exception e) {
            return ResponseObject.errorForward(
                    ResponseMessage.INTERNAL_SERVER_ERROR.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Override
    public ResponseObject createGpu(ModifyGpuRequest request) {
        if (request.getGpuModel().length() > 255) {
            return ResponseObject.errorForward(ResponseMessage.STRING_TOO_LONG.getMessage(), HttpStatus.BAD_REQUEST);
        }
        request.setGpuModel(request.getGpuModel().replaceAll("\\s+", " "));
        boolean isExistsGpuCode = gpuExtendRepository.existsByModel(request.getGpuModel().trim());

        if (isExistsGpuCode) {
            return ResponseObject.errorForward(
                    ResponseMessage.DUPLICATE.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        } else {
            try {
                Gpu gpu = new Gpu();
                gpu.setModel(request.getGpuModel().trim());
                gpu.setManufacturer(request.getGpuManufacturer());
                gpu.setSeries(request.getGpuSeries().trim());
                gpu.setMemorySize(request.getGpuMemorySize());
                gpu.setIntegrated(request.isGpuIsIntegrated());
                gpu.setEntityStatus(EntityStatus.NOT_DELETED);
                return new ResponseObject<>(
                        gpuExtendRepository.save(gpu),
                        HttpStatus.OK,
                        ResponseMessage.CREATED.getMessage()
                );
            } catch (NumberFormatException e) {
                return ResponseObject.errorForward(ResponseMessage.INVALID_NUMBER.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
    }

    @Override
    public ResponseObject updateGpu(ModifyGpuRequest request, String id) {
        if (request.getGpuModel().length() > 255) {
            return ResponseObject.errorForward(ResponseMessage.STRING_TOO_LONG.getMessage(), HttpStatus.BAD_REQUEST);
        }
        request.setGpuModel(request.getGpuModel().replaceAll("\\s+", " "));

        Optional<Gpu> gpuOptional = gpuExtendRepository.findById(id);

        if (gpuOptional.isEmpty()) {
            return ResponseObject.errorForward(ResponseMessage.NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!gpuOptional.get().getModel().trim().equalsIgnoreCase(request.getGpuModel().trim())) {
            if (gpuExtendRepository.existsByModel(request.getGpuModel().trim())) {
                return ResponseObject.errorForward(
                        ResponseMessage.DUPLICATE.getMessage(),
                        HttpStatus.BAD_REQUEST);
            }
        }


        Gpu gpu = gpuOptional.get();
        try {
            gpu.setModel(request.getGpuModel().trim());
            gpu.setManufacturer(request.getGpuManufacturer());
            gpu.setSeries(request.getGpuSeries().trim());
            gpu.setMemorySize(request.getGpuMemorySize());
            gpu.setIntegrated(request.isGpuIsIntegrated());
            gpu.setEntityStatus(EntityStatus.NOT_DELETED);
            return new ResponseObject<>(
                    gpuExtendRepository.save(gpu),
                    HttpStatus.OK,
                    ResponseMessage.UPDATED.getMessage()
            );
        } catch (NumberFormatException e) {
            return ResponseObject.errorForward(ResponseMessage.INVALID_NUMBER.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseObject updateGpuStatus(String id) {
        Optional<Gpu> gpuOptional = gpuExtendRepository.findById(id);
        if (gpuOptional.isPresent()) {
            Gpu gpu = gpuOptional.get();
            if (gpu.getEntityStatus().equals(EntityStatus.NOT_DELETED)) {
                gpu.setEntityStatus(EntityStatus.DELETED);
            } else {
                gpu.setEntityStatus(EntityStatus.NOT_DELETED);
            }
            return new ResponseObject<>(
                    gpuExtendRepository.save(gpu),
                    HttpStatus.OK,
                    ResponseMessage.UPDATED.getMessage()
            );
        } else {
            return ResponseObject.errorForward(ResponseMessage.NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
