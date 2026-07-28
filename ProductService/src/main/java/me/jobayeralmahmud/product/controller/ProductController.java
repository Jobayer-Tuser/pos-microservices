package me.jobayeralmahmud.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.annotations.ApiResponseMessage;
import me.jobayeralmahmud.product.entity.Product;
import me.jobayeralmahmud.product.request.CreateProductRequest;
import me.jobayeralmahmud.product.request.UpdateProductRequest;
import me.jobayeralmahmud.product.response.PaginateProduct;
import me.jobayeralmahmud.product.response.ProductDto;
import me.jobayeralmahmud.product.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dev/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @ApiResponseMessage("Products retrieved successfully")
    public PaginateProduct<ProductDto> index(@RequestParam(required = false) UUID lastId, Pageable pageable) {
        return productService.getAllProducts(lastId, pageable);
    }

    @PostMapping
    @ApiResponseMessage("Product created successfully")
    public ProductDto store(@Valid @RequestBody CreateProductRequest request) {
        return productService.createProduct(request);
    }

    @GetMapping("/{id}")
    @ApiResponseMessage("Products retrieved successfully")
    public Product show(@PathVariable("id") UUID productId) {
        return productService.getProductById(productId);
    }

    @PatchMapping("/{id}")
    @ApiResponseMessage("Product updated successfully")
    public Product update(@PathVariable UUID id, @Valid @RequestBody UpdateProductRequest request) {
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @ApiResponseMessage("Product deleted successfully")
    public void destroy(@PathVariable UUID id) {
        productService.deleteProduct(id);
    }
}