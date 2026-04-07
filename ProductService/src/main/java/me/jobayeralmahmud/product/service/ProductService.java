package me.jobayeralmahmud.product.service;

import me.jobayeralmahmud.product.entity.Product;
import me.jobayeralmahmud.product.request.CreateProductRequest;
import me.jobayeralmahmud.product.request.UpdateProductRequest;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product createProduct(CreateProductRequest request);
    Product updateProduct(Long id, UpdateProductRequest request);
    void deleteProduct(Long id);
}