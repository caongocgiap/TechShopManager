package com.techshop.server.core.feature.admin.category.controller;

import com.techshop.server.core.feature.admin.category.model.request.CategoryPaginationRequest;
import com.techshop.server.core.feature.admin.category.model.request.ModifyCategoryRequest;
import com.techshop.server.core.feature.admin.category.service.CategoryService;
import com.techshop.server.infrastructure.constant.ApiConstant;
import com.techshop.server.util.Helper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstant.API_CATEGORY_PREFIX)
@RequiredArgsConstructor
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> get(CategoryPaginationRequest request) {
        return Helper.createResponseEntity(categoryService.getPaginationCategories(request));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody ModifyCategoryRequest modifyCategoryRequest) {
        return Helper.createResponseEntity(categoryService.createCategory(modifyCategoryRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @Valid @RequestBody ModifyCategoryRequest modifyCategoryRequest,
            @PathVariable String id
    ) {
        return Helper.createResponseEntity(categoryService.updateCategory(modifyCategoryRequest, id));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateCategoryStatus(@PathVariable String id) {
        return Helper.createResponseEntity(categoryService.updateCategoryStatus(id));
    }

}
