package me.jobayeralmahmud.product.service;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.product.entity.ProductImage;
import me.jobayeralmahmud.product.repository.ProductImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository repository;

    @Override
    public void storeProductImage(List<ProductImage> productImages) {
        repository.saveAll(productImages);
    }
}