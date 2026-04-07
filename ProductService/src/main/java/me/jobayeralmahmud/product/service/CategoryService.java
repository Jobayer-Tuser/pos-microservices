package me.jobayeralmahmud.product.service;

import me.jobayeralmahmud.product.entity.Category;
import me.jobayeralmahmud.product.request.CreateCategoryRequest;
import me.jobayeralmahmud.product.request.UpdateCategoryRequest;
import me.jobayeralmahmud.product.response.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    Category getCategoryById(Long id);
    Category createCategory(CreateCategoryRequest request);
    Category updateCategory(Long id, UpdateCategoryRequest request);
    void deleteCategory(Long id);

    Category getCategoryReference(Long id);
}