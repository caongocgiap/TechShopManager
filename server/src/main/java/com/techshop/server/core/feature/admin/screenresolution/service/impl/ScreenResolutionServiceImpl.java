package com.techshop.server.core.feature.admin.screenresolution.service.impl;

import com.techshop.server.core.common.PageableObject;
import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.core.feature.admin.screenresolution.model.request.ModifyScreenResolutionRequest;
import com.techshop.server.core.feature.admin.screenresolution.model.request.ScreenResolutionPaginationRequest;
import com.techshop.server.core.feature.admin.screenresolution.repository.ScreenResolutionExtendRepository;
import com.techshop.server.core.feature.admin.screenresolution.service.ScreenResolutionService;
import com.techshop.server.entity.ScreenResolution;
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
public class ScreenResolutionServiceImpl implements ScreenResolutionService {

    private final ScreenResolutionExtendRepository screenResolutionExtendRepository;

    @Override
    public ResponseObject getPaginationScreenResolution(ScreenResolutionPaginationRequest request) {
        try {
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
            if (request.getSearchValues() == null) {
                return new ResponseObject<>(
                        PageableObject.of(screenResolutionExtendRepository.search(pageable)),
                        HttpStatus.OK,
                        ResponseMessage.SUCCESS.getMessage()
                );
            }
            return new ResponseObject<>(
                    PageableObject.of(screenResolutionExtendRepository.search(pageable, request)),
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
    public ResponseObject createScreenResolution(ModifyScreenResolutionRequest request) {
        if (request.getScreenResolutionName().length() > 255) {
            return ResponseObject.errorForward(ResponseMessage.STRING_TOO_LONG.getMessage(), HttpStatus.BAD_REQUEST);
        }
        request.setScreenResolutionName(request.getScreenResolutionName().replaceAll("\\s+", " "));
        boolean isExistsScreenResolutionCode = screenResolutionExtendRepository.existsByCode(request.getScreenResolutionCode().trim());

        if (isExistsScreenResolutionCode) {
            return ResponseObject.errorForward(
                    ResponseMessage.DUPLICATE.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        } else {
            try {
                ScreenResolution screenResolution = new ScreenResolution();
                screenResolution.setCode(request.getScreenResolutionCode().trim());
                screenResolution.setName(request.getScreenResolutionName().trim());
                screenResolution.setWidth(Long.parseLong(request.getScreenResolutionWidth().trim()));
                screenResolution.setHeight(Long.parseLong(request.getScreenResolutionHeight().trim()));
                screenResolution.setAspectRatio(screenResolution.calculateAspectRatio());
                screenResolution.setEntityStatus(EntityStatus.NOT_DELETED);
                return new ResponseObject<>(
                        screenResolutionExtendRepository.save(screenResolution),
                        HttpStatus.OK,
                        ResponseMessage.CREATED.getMessage()
                );
            } catch (NumberFormatException e) {
                return ResponseObject.errorForward(ResponseMessage.INVALID_NUMBER.getMessage(), HttpStatus.BAD_REQUEST);
            }

        }
    }

    @Override
    public ResponseObject updateScreenResolution(ModifyScreenResolutionRequest request, String id) {
        if (request.getScreenResolutionName().length() > 255) {
            return ResponseObject.errorForward(ResponseMessage.STRING_TOO_LONG.getMessage(), HttpStatus.BAD_REQUEST);
        }
        request.setScreenResolutionName(request.getScreenResolutionName().replaceAll("\\s+", " "));

        Optional<ScreenResolution> screenResolutionOptional = screenResolutionExtendRepository.findById(id);

        if (screenResolutionOptional.isEmpty()) {
            return ResponseObject.errorForward(ResponseMessage.NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!screenResolutionOptional.get().getCode().trim().equalsIgnoreCase(request.getScreenResolutionCode().trim())) {
            if (screenResolutionExtendRepository.existsByCode(request.getScreenResolutionCode().trim())) {
                return ResponseObject.errorForward(
                        ResponseMessage.DUPLICATE.getMessage(),
                        HttpStatus.BAD_REQUEST);
            }
        }

        ScreenResolution screenResolution = screenResolutionOptional.get();
        try {
            screenResolution.setId(id);
            screenResolution.setCode(request.getScreenResolutionCode().trim());
            screenResolution.setName(request.getScreenResolutionName().trim());
            screenResolution.setWidth(Long.parseLong(request.getScreenResolutionWidth().trim()));
            screenResolution.setHeight(Long.parseLong(request.getScreenResolutionHeight().trim()));
            screenResolution.setAspectRatio(screenResolution.calculateAspectRatio());
            return new ResponseObject<>(
                    screenResolutionExtendRepository.save(screenResolution),
                    HttpStatus.OK,
                    ResponseMessage.UPDATED.getMessage()
            );
        } catch (NumberFormatException e) {
            return ResponseObject.errorForward(ResponseMessage.INVALID_NUMBER.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseObject updateScreenResolutionStatus(String id) {
        Optional<ScreenResolution> screenResolutionOptional = screenResolutionExtendRepository.findById(id);
        if (screenResolutionOptional.isPresent()) {
            ScreenResolution screenResolution = screenResolutionOptional.get();
            if (screenResolution.getEntityStatus().equals(EntityStatus.NOT_DELETED)) {
                screenResolution.setEntityStatus(EntityStatus.DELETED);
            } else {
                screenResolution.setEntityStatus(EntityStatus.NOT_DELETED);
            }
            return new ResponseObject<>(
                    screenResolutionExtendRepository.save(screenResolution),
                    HttpStatus.OK,
                    ResponseMessage.UPDATED.getMessage()
            );
        } else {
            return ResponseObject.errorForward(ResponseMessage.NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
