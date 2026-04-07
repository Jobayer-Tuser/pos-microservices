package me.jobayeralmahmud.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.controller.BaseController;
import me.jobayeralmahmud.product.request.CreateCategoryRequest;
import me.jobayeralmahmud.product.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dev/api/v1/products")
public class CategoryController extends BaseController {

    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> index() {
        return ok(categoryService.getAllCategories(), "Categories retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<?> store(@Valid @RequestBody CreateCategoryRequest request) {
        return created(categoryService.createCategory(request), "Category created successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id) {
        return ResponseEntity.ok("Hello World");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        return ResponseEntity.ok("Hello World");
    }

}