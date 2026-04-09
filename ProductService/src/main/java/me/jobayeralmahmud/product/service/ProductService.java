package me.jobayeralmahmud.product.service;

import me.jobayeralmahmud.product.entity.Product;
import me.jobayeralmahmud.product.request.CreateProductRequest;
import me.jobayeralmahmud.product.request.UpdateProductRequest;
import me.jobayeralmahmud.product.response.ProductDto;
import me.jobayeralmahmud.product.response.PaginateProduct;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {
    PaginateProduct<ProductDto> getAllProducts(UUID lastId, Pageable pageable);
    ProductDto createProduct(CreateProductRequest request);
    Product getProductById(UUID id);
    Product updateProduct(UUID id, UpdateProductRequest request);
    void deleteProduct(UUID id);
}