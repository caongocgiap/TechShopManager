package com.techshop.server.core.feature.admin.cpu.service.impl;

import com.techshop.server.core.common.PageableObject;
import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.core.feature.admin.cpu.model.request.CpuPaginationRequest;
import com.techshop.server.core.feature.admin.cpu.model.request.ModifyCpuRequest;
import com.techshop.server.core.feature.admin.cpu.model.response.ModifyCpuSuccess;
import com.techshop.server.core.feature.admin.cpu.repository.CpuExtendRepository;
import com.techshop.server.core.feature.admin.cpu.service.CpuService;
import com.techshop.server.entity.Cpu;
import com.techshop.server.entity.Manufacturer;
import com.techshop.server.infrastructure.constant.EntityStatus;
import com.techshop.server.infrastructure.constant.ResponseMessage;
import com.techshop.server.repository.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CpuServiceImpl implements CpuService {

    private final CpuExtendRepository cpuExtendRepository;
    private final ManufacturerRepository manufacturerRepository;

    @Override
    public ResponseObject<?> getPaginationCpu(CpuPaginationRequest request) {
        try {
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
            if (request.getSearchValues() == null) {
                return new ResponseObject<>(
                        PageableObject.of(cpuExtendRepository.search(pageable)),
                        HttpStatus.OK,
                        ResponseMessage.SUCCESS.getMessage()
                );
            }
            return new ResponseObject<>(
                    PageableObject.of(cpuExtendRepository.search(pageable, request)),
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
    public ResponseObject<?> createCpu(ModifyCpuRequest request) {
        try {
            Optional<Manufacturer> manufacturerOptional = manufacturerRepository.findById(request.getCpuManufacturerId());

            if (cpuExtendRepository.existsByModel(request.getCpuModel().trim())) {
                return ResponseObject.errorForward(
                        ResponseMessage.DUPLICATE.getMessage(),
                        HttpStatus.BAD_REQUEST
                );
            }

            if (manufacturerOptional.isPresent()) {
                Cpu cpu = new Cpu();
                cpu.setModel(request.getCpuModel().trim());
                cpu.setManufacturer(manufacturerOptional.get());
                cpu.setGeneration(request.getCpuGeneration().trim());
                cpu.setCores(request.getCpuCores());
                cpu.setThreads(request.getCpuThreads());
                cpu.setBaseClock(request.getCpuBaseClock());
                cpu.setTurboClock(request.getCpuTurboClock());
                cpu.setCacheSize(request.getCpuCacheSize());
                cpu.setTdpWatt(request.getCpuTdpWatt());
                cpu.setEntityStatus(EntityStatus.NOT_DELETED);
                cpuExtendRepository.save(cpu);
                return new ResponseObject<>(new ModifyCpuSuccess(
                        cpu.getId(),
                        cpu.getModel(),
                        cpu.getGeneration(),
                        cpu.getCores(),
                        cpu.getThreads(),
                        cpu.getBaseClock(),
                        cpu.getTurboClock(),
                        cpu.getCacheSize(),
                        cpu.getTdpWatt(),
                        cpu.getManufacturer().getName()
                ),
                        HttpStatus.OK,
                        ResponseMessage.CREATED.getMessage()
                );
            } else {
                return ResponseObject.errorForward(
                        ResponseMessage.NOT_FOUND.getMessage() + " nhà sản xuất",
                        HttpStatus.BAD_REQUEST
                );
            }
        } catch (Exception e) {
            return ResponseObject.errorForward(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseObject<?> updateCpu(ModifyCpuRequest request, String id) {
        try {
            Optional<Manufacturer> manufacturerOptional = manufacturerRepository.findById(id);
            Optional<Cpu> cpuOptional = cpuExtendRepository.findById(id);

            if (cpuOptional.isEmpty()) {
                return ResponseObject.errorForward(ResponseMessage.NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);
            }

            if (!cpuOptional.get().getModel().trim().equalsIgnoreCase(request.getCpuModel().trim())) {
                if (cpuExtendRepository.existsByModel(request.getCpuModel().trim())) {
                    return ResponseObject.errorForward(
                            ResponseMessage.DUPLICATE.getMessage(),
                            HttpStatus.BAD_REQUEST);
                }
            }

            if (manufacturerOptional.isPresent()) {
                Cpu cpu = cpuOptional.get();
                cpu.setModel(request.getCpuModel().trim());
                cpu.setManufacturer(manufacturerOptional.get());
                cpu.setGeneration(request.getCpuGeneration().trim());
                cpu.setCores(request.getCpuCores());
                cpu.setThreads(request.getCpuThreads());
                cpu.setBaseClock(request.getCpuBaseClock());
                cpu.setTurboClock(request.getCpuTurboClock());
                cpu.setCacheSize(request.getCpuCacheSize());
                cpu.setTdpWatt(request.getCpuTdpWatt());
                cpu.setEntityStatus(EntityStatus.NOT_DELETED);
                cpuExtendRepository.save(cpu);
                return new ResponseObject<>(new ModifyCpuSuccess(
                        cpu.getId(),
                        cpu.getModel(),
                        cpu.getGeneration(),
                        cpu.getCores(),
                        cpu.getThreads(),
                        cpu.getBaseClock(),
                        cpu.getTurboClock(),
                        cpu.getCacheSize(),
                        cpu.getTdpWatt(),
                        cpu.getManufacturer().getName()
                ),
                        HttpStatus.OK,
                        ResponseMessage.UPDATED.getMessage()
                );
            } else {
                return ResponseObject.errorForward(
                        ResponseMessage.NOT_FOUND.getMessage() + " nhà sản xuất",
                        HttpStatus.BAD_REQUEST
                );
            }
        } catch (Exception e) {
            return ResponseObject.errorForward(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseObject updateCpuStatus(String id) {
        Optional<Cpu> cpuOptional = cpuExtendRepository.findById(id);
        if (cpuOptional.isPresent()) {
            Cpu cpu = cpuOptional.get();
            if (cpu.getEntityStatus().equals(EntityStatus.NOT_DELETED)) {
                cpu.setEntityStatus(EntityStatus.DELETED);
            } else {
                cpu.setEntityStatus(EntityStatus.NOT_DELETED);
            }
            return new ResponseObject<>(
                    cpuExtendRepository.save(cpu),
                    HttpStatus.OK,
                    ResponseMessage.UPDATED.getMessage()
            );
        } else {
            return ResponseObject.errorForward(ResponseMessage.NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
