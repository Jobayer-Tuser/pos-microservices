package me.jobayeralmahmud.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.controller.BaseController;
import me.jobayeralmahmud.product.request.CreateCategoryRequest;
import me.jobayeralmahmud.product.request.UpdateCategoryRequest;
import me.jobayeralmahmud.product.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dev/api/v1/categories")
public class CategoryController extends BaseController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> index() {
        return ok(categoryService.getAllCategories(), "Categories retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<?> store(@Valid @RequestBody CreateCategoryRequest request) {
        return created(categoryService.createCategory(request), "Category created successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateCategoryRequest request) {
        return ok(categoryService.updateCategory(id, request), "Category updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return noContent("Category deleted successfully");
    }

}