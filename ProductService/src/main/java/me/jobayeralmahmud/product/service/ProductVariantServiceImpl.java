package me.jobayeralmahmud.product.service;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.product.entity.ProductVariant;
import me.jobayeralmahmud.product.repository.ProductVariantRepository;
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