package me.jobayeralmahmud.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.annotations.ApiResponseMessage;
import me.jobayeralmahmud.product.entity.Category;
import me.jobayeralmahmud.product.request.CreateCategoryRequest;
import me.jobayeralmahmud.product.request.UpdateCategoryRequest;
import me.jobayeralmahmud.product.response.CategoryDto;
import me.jobayeralmahmud.product.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dev/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @ApiResponseMessage("Categories retrieved successfully")
    public List<CategoryDto> index() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    @ApiResponseMessage("Category created successfully")
    public Category store(@Valid @RequestBody CreateCategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @PatchMapping("/{id}")
    @ApiResponseMessage("Category updated successfully")
    public Category update(@PathVariable UUID id, @Valid @RequestBody UpdateCategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }

    @DeleteMapping("/{id}")
    @ApiResponseMessage("Category deleted successfully")
    public void destroy(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
    }
}