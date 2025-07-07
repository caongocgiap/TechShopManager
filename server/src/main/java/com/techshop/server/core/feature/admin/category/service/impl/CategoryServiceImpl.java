package com.techshop.server.core.feature.admin.category.service.impl;

import com.techshop.server.core.common.PageableObject;
import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.core.feature.admin.category.model.request.CategoryPaginationRequest;
import com.techshop.server.core.feature.admin.category.model.request.ModifyCategoryRequest;
import com.techshop.server.core.feature.admin.category.repository.CategoryExtendRepository;
import com.techshop.server.core.feature.admin.category.service.CategoryService;
import com.techshop.server.entity.Category;
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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryExtendRepository categoryExtendRepository;

    @Override
    public ResponseObject getPaginationCategories(CategoryPaginationRequest request) {
        try {
            Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
            if (request.getSearchValues() == null) {
                return new ResponseObject<>(
                        PageableObject.of(categoryExtendRepository.search(pageable)),
                        HttpStatus.OK,
                        ResponseMessage.SUCCESS.getMessage()
                );
            }
            return new ResponseObject<>(
                    PageableObject.of(categoryExtendRepository.search(pageable, request)),
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
    public ResponseObject createCategory(ModifyCategoryRequest request) {
        if (request.getCategoryName().length() > 255) {
            return ResponseObject.errorForward(ResponseMessage.STRING_TOO_LONG.getMessage(), HttpStatus.BAD_REQUEST);
        }
        request.setCategoryName(request.getCategoryName().replaceAll("\\s+", " "));
        boolean isExistsCategoryCode = categoryExtendRepository.existsByCode(request.getCategoryCode().trim());

        if (isExistsCategoryCode) {
            return ResponseObject.errorForward(
                    ResponseMessage.DUPLICATE.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        } else {
            Category category = new Category();
            category.setCode(request.getCategoryCode().trim());
            category.setName(request.getCategoryName().trim());
            category.setEntityStatus(EntityStatus.NOT_DELETED);
            return new ResponseObject<>(
                    categoryExtendRepository.save(category),
                    HttpStatus.OK,
                    ResponseMessage.CREATED.getMessage()
            );
        }
    }

    @Override
    public ResponseObject updateCategory(ModifyCategoryRequest request, String id) {
        if (request.getCategoryName().length() > 255) {
            return ResponseObject.errorForward(ResponseMessage.STRING_TOO_LONG.getMessage(), HttpStatus.BAD_REQUEST);
        }
        request.setCategoryName(request.getCategoryName().replaceAll("\\s+", " "));

        Optional<Category> categoryOptional = categoryExtendRepository.findById(id);

        if (categoryOptional.isEmpty()) {
            return ResponseObject.errorForward(ResponseMessage.NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!categoryOptional.get().getCode().trim().equalsIgnoreCase(request.getCategoryCode().trim())) {
            if (categoryExtendRepository.existsByCode(request.getCategoryCode().trim())) {
                return ResponseObject.errorForward(
                        ResponseMessage.DUPLICATE.getMessage(),
                        HttpStatus.BAD_REQUEST);
            }
        }

        categoryOptional.get().setId(id);
        categoryOptional.get().setCode(request.getCategoryCode().trim());
        categoryOptional.get().setName(request.getCategoryName().trim());
        return new ResponseObject<>(
                categoryExtendRepository.save(categoryOptional.get()),
                HttpStatus.OK,
                ResponseMessage.UPDATED.getMessage()
        );
    }

    @Override
    public ResponseObject updateCategoryStatus(String id) {
        Optional<Category> categoryOptional = categoryExtendRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            if (category.getEntityStatus().equals(EntityStatus.NOT_DELETED)) {
                category.setEntityStatus(EntityStatus.DELETED);
            } else {
                category.setEntityStatus(EntityStatus.NOT_DELETED);
            }
            return new ResponseObject<>(
                    categoryExtendRepository.save(category),
                    HttpStatus.OK,
                    ResponseMessage.UPDATED.getMessage()
            );
        } else {
            return ResponseObject.errorForward(ResponseMessage.NOT_FOUND.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
