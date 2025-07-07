package com.techshop.server.core.feature.admin.brand.service.impl;

import com.techshop.server.core.common.PageableObject;
import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.core.feature.admin.brand.model.request.BrandPaginationRequest;
import com.techshop.server.core.feature.admin.brand.model.request.ModifyBrandRequest;
import com.techshop.server.core.feature.admin.brand.repository.BrandExtendRepository;
import com.techshop.server.core.feature.admin.brand.service.BrandService;
import com.techshop.server.entity.Brand;
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
public class BrandServiceImpl implements BrandService {

    private final BrandExtendRepository brandExtendRepository;

    @Override
    public ResponseObject getPaginationBrands(BrandPaginationRequest request) {
        try {
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
            if(request.getBrandCode() == null && request.getBrandName() == null) {
                return new ResponseObject<>(
                        PageableObject.of(brandExtendRepository.search(pageable)),
                        HttpStatus.OK,
                        ResponseMessage.SUCCESS.getMessage()
                );
            }
            return new ResponseObject<>(
                    PageableObject.of(brandExtendRepository.search(pageable, request)),
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
    public ResponseObject createBrand(ModifyBrandRequest request) {
        if (request.getBrandName().length() > 255) {
            return ResponseObject.errorForward(ResponseMessage.STRING_TOO_LONG.getMessage(), HttpStatus.BAD_REQUEST);
        }
        request.setBrandName(request.getBrandName().replaceAll("\\s+", " "));
        boolean isExistsBrandCode = brandExtendRepository.existsByCode(request.getBrandCode().trim());

        if (isExistsBrandCode) {
            return ResponseObject.errorForward(
                    ResponseMessage.DUPLICATE.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        } else {
            Brand brand = new Brand();
            brand.setCode(request.getBrandCode().trim());
            brand.setName(request.getBrandName().trim());
            brand.setEntityStatus(EntityStatus.NOT_DELETED);
            return new ResponseObject<>(
                    brandExtendRepository.save(brand),
                    HttpStatus.OK,
                    ResponseMessage.CREATED.getMessage()
            );
        }
    }

    @Override
    public ResponseObject updateBrand(ModifyBrandRequest request, String id) {
        if (request.getBrandName().length() > 255) {
            return ResponseObject.errorForward(ResponseMessage.STRING_TOO_LONG.getMessage(), HttpStatus.BAD_REQUEST);
        }
        request.setBrandName(request.getBrandName().replaceAll("\\s+", " "));

        Optional<Brand> brandOptional = brandExtendRepository.findById(id);

        if (brandOptional.isEmpty()) {
            return ResponseObject.errorForward(ResponseMessage.NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!brandOptional.get().getCode().trim().equalsIgnoreCase(request.getBrandCode().trim())) {
            if (brandExtendRepository.existsByCode(request.getBrandCode().trim())) {
                return ResponseObject.errorForward(
                        ResponseMessage.DUPLICATE.getMessage(),
                        HttpStatus.BAD_REQUEST);
            }
        }

        brandOptional.get().setId(id);
        brandOptional.get().setCode(request.getBrandCode().trim());
        brandOptional.get().setName(request.getBrandName().trim());
        return new ResponseObject<>(
                brandExtendRepository.save(brandOptional.get()),
                HttpStatus.OK,
                ResponseMessage.UPDATED.getMessage()
        );
    }

    @Override
    public ResponseObject updateBrandStatus(String id) {
        Optional<Brand> brandOptional = brandExtendRepository.findById(id);
        if (brandOptional.isPresent()) {
            Brand brand = brandOptional.get();
            if (brand.getEntityStatus().equals(EntityStatus.NOT_DELETED)) {
                brand.setEntityStatus(EntityStatus.DELETED);
            } else {
                brand.setEntityStatus(EntityStatus.NOT_DELETED);
            }
            return new ResponseObject<>(
                    brandExtendRepository.save(brand),
                    HttpStatus.OK,
                    ResponseMessage.UPDATED.getMessage()
            );
        } else {
            return ResponseObject.errorForward(ResponseMessage.NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
