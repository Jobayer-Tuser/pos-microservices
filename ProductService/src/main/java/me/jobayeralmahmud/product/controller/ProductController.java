package me.jobayeralmahmud.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.controller.BaseController;
import me.jobayeralmahmud.product.request.CreateProductRequest;
import me.jobayeralmahmud.product.request.UpdateProductRequest;
import me.jobayeralmahmud.product.response.ProductDto;
import me.jobayeralmahmud.product.response.PaginateProduct;
import me.jobayeralmahmud.product.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dev/api/v1/products")
public class ProductController extends BaseController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> index(@RequestParam(required = false) UUID lastId, Pageable pageable) {
        PaginateProduct<ProductDto> products = productService.getAllProducts(lastId, pageable);
        return ok(products, "Products retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<?> store(@Valid @RequestBody CreateProductRequest request) {
        return created(productService.createProduct(request), "Product created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable("id") UUID productId) {
        return ok(productService.getProductById(productId), "Products retrieved successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @RequestBody UpdateProductRequest request) {
        return ok(productService.updateProduct(id, request), "Product updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return noContent("Product deleted successfully");
    }
}