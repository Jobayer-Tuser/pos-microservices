package me.jobayeralmahmud.product.service;

import me.jobayeralmahmud.product.entity.Category;
import me.jobayeralmahmud.product.request.CreateCategoryRequest;
import me.jobayeralmahmud.product.request.UpdateCategoryRequest;
import me.jobayeralmahmud.product.response.CategoryDto;

import java.util.List;

import java.util.UUID;

public interface CategoryService {

    /**
     * Get all categories
     *
     * @return List of CategoryDto
     */
    List<CategoryDto> getAllCategories();

    /**
     * Get category by id
     *
     * @param id UUID of the category
     * @return Category entity
     */
    Category getCategoryById(UUID id);

    /**
     * Create a new category
     *
     * @param request CreateCategoryRequest containing category details
     * @return Created Category entity
     */
    Category createCategory(CreateCategoryRequest request);

    /**
     * Update an existing category
     *
     * @param id UUID of the category to update
     * @param request UpdateCategoryRequest containing updated category details
     * @return Updated Category entity
     */
    Category updateCategory(UUID id, UpdateCategoryRequest request);

    /**
     * Delete a category by id
     *
     * @param id UUID of the category to delete
     */
    void deleteCategory(UUID id);

    /**
     * Get a reference to a category by id (this will not hit the database immediately, useful for associations)
     *
     * @param id UUID of the category
     * @return Category entity reference
     */
    Category getCategoryReference(UUID id);
}