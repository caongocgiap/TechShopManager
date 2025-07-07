package com.techshop.server.core.feature.admin.operationsystem.service.impl;

import com.techshop.server.core.common.PageableObject;
import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.core.feature.admin.operationsystem.model.request.ModifyOperationSystemRequest;
import com.techshop.server.core.feature.admin.operationsystem.model.request.OperationSystemPaginationRequest;
import com.techshop.server.core.feature.admin.operationsystem.repository.OperationSystemExtendRepository;
import com.techshop.server.core.feature.admin.operationsystem.service.OperationSystemService;
import com.techshop.server.entity.OperationSystem;
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
public class OperationSystemServiceImpl implements OperationSystemService {

    private final OperationSystemExtendRepository operationSystemExtendRepository;

    @Override
    public ResponseObject getPaginationOperationSystem(OperationSystemPaginationRequest request) {
        try {
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
            if (request.getSearchValues() == null) {
                return new ResponseObject<>(
                        PageableObject.of(operationSystemExtendRepository.search(pageable)),
                        HttpStatus.OK,
                        ResponseMessage.SUCCESS.getMessage()
                );
            }
            return new ResponseObject<>(
                    PageableObject.of(operationSystemExtendRepository.search(pageable, request)),
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
    public ResponseObject createOperationSystem(ModifyOperationSystemRequest request) {
        if (request.getOperationSystemName().length() > 255) {
            return ResponseObject.errorForward(ResponseMessage.STRING_TOO_LONG.getMessage(), HttpStatus.BAD_REQUEST);
        }
        request.setOperationSystemName(request.getOperationSystemName().replaceAll("\\s+", " "));
        boolean isExistsOperationSystemCode = operationSystemExtendRepository.existsByCode(request.getOperationSystemCode().trim());

        if (isExistsOperationSystemCode) {
            return ResponseObject.errorForward(
                    ResponseMessage.DUPLICATE.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        } else {
            OperationSystem operationSystem = new OperationSystem();
            operationSystem.setCode(request.getOperationSystemCode().trim());
            operationSystem.setName(request.getOperationSystemName().trim());
            operationSystem.setEntityStatus(EntityStatus.NOT_DELETED);
            return new ResponseObject<>(
                    operationSystemExtendRepository.save(operationSystem),
                    HttpStatus.OK,
                    ResponseMessage.CREATED.getMessage()
            );
        }
    }

    @Override
    public ResponseObject updateOperationSystem(ModifyOperationSystemRequest request, String id) {
        if (request.getOperationSystemName().length() > 255) {
            return ResponseObject.errorForward(ResponseMessage.STRING_TOO_LONG.getMessage(), HttpStatus.BAD_REQUEST);
        }
        request.setOperationSystemName(request.getOperationSystemName().replaceAll("\\s+", " "));

        Optional<OperationSystem> operationSystemOptional = operationSystemExtendRepository.findById(id);

        if (operationSystemOptional.isEmpty()) {
            return ResponseObject.errorForward(ResponseMessage.NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!operationSystemOptional.get().getCode().trim().equalsIgnoreCase(request.getOperationSystemCode().trim())) {
            if (operationSystemExtendRepository.existsByCode(request.getOperationSystemCode().trim())) {
                return ResponseObject.errorForward(
                        ResponseMessage.DUPLICATE.getMessage(),
                        HttpStatus.BAD_REQUEST);
            }
        }

        operationSystemOptional.get().setId(id);
        operationSystemOptional.get().setCode(request.getOperationSystemCode().trim());
        operationSystemOptional.get().setName(request.getOperationSystemName().trim());
        return new ResponseObject<>(
                operationSystemExtendRepository.save(operationSystemOptional.get()),
                HttpStatus.OK,
                ResponseMessage.UPDATED.getMessage()
        );
    }

    @Override
    public ResponseObject updateOperationSystemStatus(String id) {
        Optional<OperationSystem> operationSystemOptional = operationSystemExtendRepository.findById(id);
        if (operationSystemOptional.isPresent()) {
            OperationSystem operationSystem = operationSystemOptional.get();
            if (operationSystem.getEntityStatus().equals(EntityStatus.NOT_DELETED)) {
                operationSystem.setEntityStatus(EntityStatus.DELETED);
            } else {
                operationSystem.setEntityStatus(EntityStatus.NOT_DELETED);
            }
            return new ResponseObject<>(
                    operationSystemExtendRepository.save(operationSystem),
                    HttpStatus.OK,
                    ResponseMessage.UPDATED.getMessage()
            );
        } else {
            return ResponseObject.errorForward(ResponseMessage.NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
