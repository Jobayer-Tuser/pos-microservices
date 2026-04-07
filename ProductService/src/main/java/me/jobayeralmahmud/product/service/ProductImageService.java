package me.jobayeralmahmud.product.service;

import me.jobayeralmahmud.product.entity.ProductImage;

import java.util.List;

public interface ProductImageService {
    void storeProductImage(List<ProductImage> productImages);
}