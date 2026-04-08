package me.jobayeralmahmud.product.service;

import me.jobayeralmahmud.library.response.CursorPageResponse;
import me.jobayeralmahmud.product.entity.Product;
import me.jobayeralmahmud.product.request.CreateProductRequest;
import me.jobayeralmahmud.product.request.UpdateProductRequest;
import me.jobayeralmahmud.product.response.ProductDto;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    CursorPageResponse<ProductDto> getAllProducts(Long lastId, Pageable pageable);
    ProductDto createProduct(CreateProductRequest request);
    Product getProductById(Long id);
    Product updateProduct(Long id, UpdateProductRequest request);
    void deleteProduct(Long id);
}