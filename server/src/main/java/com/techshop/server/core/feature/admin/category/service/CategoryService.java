package com.techshop.server.core.feature.admin.category.service;

import com.techshop.server.core.common.ResponseObject;
import com.techshop.server.core.feature.admin.category.model.request.CategoryPaginationRequest;
import com.techshop.server.core.feature.admin.category.model.request.ModifyCategoryRequest;

public interface CategoryService {

    ResponseObject getPaginationCategories(CategoryPaginationRequest categoryPaginationRequest);

    ResponseObject createCategory(ModifyCategoryRequest modifyCategoryRequest);

    ResponseObject updateCategory(ModifyCategoryRequest modifyCategoryRequest, String idCategory);

    ResponseObject updateCategoryStatus(String idCategory);

}
