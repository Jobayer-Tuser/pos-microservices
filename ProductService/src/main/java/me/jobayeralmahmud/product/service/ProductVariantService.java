package me.jobayeralmahmud.product.service;

import me.jobayeralmahmud.product.entity.ProductVariant;

import java.util.List;

public interface ProductVariantService {
    void storeProductVariant(List<ProductVariant> productVariant);
}