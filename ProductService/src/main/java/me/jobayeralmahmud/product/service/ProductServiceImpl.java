package me.jobayeralmahmud.product.service;

import me.jobayeralmahmud.product.entity.Product;
import me.jobayeralmahmud.product.request.CreateProductRequest;
import me.jobayeralmahmud.product.request.UpdateProductRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public Product getProductById(Long id) {
        return null;
    }

    @Override
    public Product createProduct(CreateProductRequest request) {
        return null;
    }

    @Override
    public Product updateProduct(Long id, UpdateProductRequest request) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }
}