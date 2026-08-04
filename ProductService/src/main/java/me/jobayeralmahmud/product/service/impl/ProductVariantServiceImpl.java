package me.jobayeralmahmud.product.service.impl;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.product.entity.ProductVariant;
import me.jobayeralmahmud.product.repository.ProductVariantRepository;
import me.jobayeralmahmud.product.service.ProductVariantService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository repository;

    @Override
    public void storeProductVariant(List<ProductVariant> productVariant) {
        repository.saveAll(productVariant);
    }
}