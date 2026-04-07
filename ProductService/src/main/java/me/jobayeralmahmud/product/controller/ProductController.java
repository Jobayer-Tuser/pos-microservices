package me.jobayeralmahmud.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.controller.BaseController;
import me.jobayeralmahmud.product.request.CreateProductRequest;
import me.jobayeralmahmud.product.request.UpdateProductRequest;
import me.jobayeralmahmud.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dev/api/v1/products")
public class ProductController extends BaseController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> index() {
        return ok(productService.getAllProducts(), "Products retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<?> store(@Valid @RequestBody CreateProductRequest request) {
        return created(productService.createProduct(request), "Product created successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        return ok(productService.updateProduct(id, request), "Product updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
        return noContent("Product deleted successfully");
    }
}