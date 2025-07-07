package com.techshop.server.core.feature.admin.manufacturer.service.impl;

import com.techshop.server.core.common.PageableObject;
import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.core.feature.admin.manufacturer.model.request.ManufacturerPaginationRequest;
import com.techshop.server.core.feature.admin.manufacturer.model.request.ModifyManufacturerRequest;
import com.techshop.server.core.feature.admin.manufacturer.repository.ManufacturerExtendRepository;
import com.techshop.server.core.feature.admin.manufacturer.service.ManufacturerService;
import com.techshop.server.entity.Manufacturer;
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
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerExtendRepository manufacturerExtendRepository;

    @Override
    public ResponseObject getAll() {
        return new ResponseObject<>(
                manufacturerExtendRepository.getAllManufacturers(),
                HttpStatus.OK,
                ResponseMessage.SUCCESS.getMessage()
        );
    }

    @Override
    public ResponseObject getPaginationManufacturer(ManufacturerPaginationRequest request) {
        try {
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
            if (request.getSearchValues() == null) {
                return new ResponseObject<>(
                        PageableObject.of(manufacturerExtendRepository.search(pageable)),
                        HttpStatus.OK,
                        ResponseMessage.SUCCESS.getMessage()
                );
            }
            return new ResponseObject<>(
                    PageableObject.of(manufacturerExtendRepository.search(pageable, request)),
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
    public ResponseObject createManufacturer(ModifyManufacturerRequest request) {
        if (request.getManufacturerCode().length() > 255) {
            return ResponseObject.errorForward(ResponseMessage.STRING_TOO_LONG.getMessage(), HttpStatus.BAD_REQUEST);
        }
        request.setManufacturerCode(request.getManufacturerCode().replaceAll("\\s+", " "));
        boolean isExistsManufacturerCode = manufacturerExtendRepository.existsByCode(request.getManufacturerCode().trim());

        if (isExistsManufacturerCode) {
            return ResponseObject.errorForward(
                    ResponseMessage.DUPLICATE.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        } else {
            Manufacturer manufacturer = new Manufacturer();
            manufacturer.setCode(request.getManufacturerCode().trim());
            manufacturer.setName(request.getManufacturerName().trim());
            manufacturer.setCountry(request.getManufacturerCountry().trim());
            manufacturer.setWebsite(request.getManufacturerWebsite().trim());
            manufacturer.setDescription(request.getManufacturerDescription().trim());
            manufacturer.setEntityStatus(EntityStatus.NOT_DELETED);
            return new ResponseObject<>(
                    manufacturerExtendRepository.save(manufacturer),
                    HttpStatus.OK,
                    ResponseMessage.CREATED.getMessage()
            );
        }
    }

    @Override
    public ResponseObject updateManufacturer(ModifyManufacturerRequest request, String id) {
        if (request.getManufacturerCode().length() > 255) {
            return ResponseObject.errorForward(ResponseMessage.STRING_TOO_LONG.getMessage(), HttpStatus.BAD_REQUEST);
        }
        request.setManufacturerCode(request.getManufacturerCode().replaceAll("\\s+", " "));

        Optional<Manufacturer> manufacturerOptional = manufacturerExtendRepository.findById(id);

        if (manufacturerOptional.isEmpty()) {
            return ResponseObject.errorForward(ResponseMessage.NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!manufacturerOptional.get().getCode().trim().equalsIgnoreCase(request.getManufacturerCode().trim())) {
            if (manufacturerExtendRepository.existsByCode(request.getManufacturerCode().trim())) {
                return ResponseObject.errorForward(
                        ResponseMessage.DUPLICATE.getMessage(),
                        HttpStatus.BAD_REQUEST);
            }
        }

        Manufacturer manufacturer = manufacturerOptional.get();
        manufacturer.setCode(request.getManufacturerCode().trim());
        manufacturer.setName(request.getManufacturerName().trim());
        manufacturer.setCountry(request.getManufacturerCountry().trim());
        manufacturer.setWebsite(request.getManufacturerWebsite().trim());
        manufacturer.setDescription(request.getManufacturerDescription().trim());
        manufacturer.setEntityStatus(EntityStatus.NOT_DELETED);
        return new ResponseObject<>(
                manufacturerExtendRepository.save(manufacturer),
                HttpStatus.OK,
                ResponseMessage.UPDATED.getMessage()
        );
    }

    @Override
    public ResponseObject updateManufacturerStatus(String id) {
        Optional<Manufacturer> manufacturerOptional = manufacturerExtendRepository.findById(id);
        if (manufacturerOptional.isPresent()) {
            Manufacturer manufacturer = manufacturerOptional.get();
            if (manufacturer.getEntityStatus().equals(EntityStatus.NOT_DELETED)) {
                manufacturer.setEntityStatus(EntityStatus.DELETED);
            } else {
                manufacturer.setEntityStatus(EntityStatus.NOT_DELETED);
            }
            return new ResponseObject<>(
                    manufacturerExtendRepository.save(manufacturer),
                    HttpStatus.OK,
                    ResponseMessage.UPDATED.getMessage()
            );
        } else {
            return ResponseObject.errorForward(ResponseMessage.NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
