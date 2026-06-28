package me.jobayeralmahmud.product.controller;

import jakarta.servlet.ServletException;
import jakarta.validation.Validator;
import me.jobayeralmahmud.library.controller.BaseHandler;
import me.jobayeralmahmud.product.request.CreateCategoryRequest;
import me.jobayeralmahmud.product.request.UpdateCategoryRequest;
import me.jobayeralmahmud.product.service.CategoryService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.util.UUID;

@Component
public class CategoryHandler extends BaseHandler {

    private final CategoryService categoryService;

    public CategoryHandler(Validator validator, CategoryService categoryService) {
        super(validator);
        this.categoryService = categoryService;
    }

    public ServerResponse index(ServerRequest request) {
        return ok(categoryService.getAllCategories(), "Categories retrieved successfully");
    }

    public ServerResponse store(ServerRequest request) throws ServletException, IOException {
        CreateCategoryRequest createRequest = request.body(CreateCategoryRequest.class);
        validate(createRequest);
        return created(categoryService.createCategory(createRequest), "Category created successfully");
    }

    public ServerResponse update(ServerRequest request) throws ServletException, IOException {
        UUID id = getUUIDPathVariable(request, "id");
        UpdateCategoryRequest updateRequest = request.body(UpdateCategoryRequest.class);
        validate(updateRequest);
        return ok(categoryService.updateCategory(id, updateRequest), "Category updated successfully");
    }

    public ServerResponse destroy(ServerRequest request) {
        UUID id = getUUIDPathVariable(request, "id");
        categoryService.deleteCategory(id);
        return noContent("Category deleted successfully");
    }
}
