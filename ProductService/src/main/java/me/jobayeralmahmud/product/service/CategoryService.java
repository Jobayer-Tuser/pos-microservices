package me.jobayeralmahmud.product.service;

import me.jobayeralmahmud.product.entity.Category;
import me.jobayeralmahmud.product.request.CreateCategoryRequest;
import me.jobayeralmahmud.product.request.UpdateCategoryRequest;
import me.jobayeralmahmud.product.response.CategoryDto;

import java.util.List;

import java.util.UUID;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    Category getCategoryById(UUID id);
    Category createCategory(CreateCategoryRequest request);
    Category updateCategory(UUID id, UpdateCategoryRequest request);
    void deleteCategory(UUID id);

    Category getCategoryReference(UUID id);
}