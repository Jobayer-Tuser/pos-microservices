package me.jobayeralmahmud.product.service;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.exceptions.ResourcesNotFoundException;
import me.jobayeralmahmud.product.entity.Product;
import me.jobayeralmahmud.product.entity.ProductVariant;
import me.jobayeralmahmud.product.repository.ProductRepository;
import me.jobayeralmahmud.product.request.CreateProductImageRequest;
import me.jobayeralmahmud.product.request.CreateProductRequest;
import me.jobayeralmahmud.product.request.CreateProductVariantRequest;
import me.jobayeralmahmud.product.request.UpdateProductRequest;
import me.jobayeralmahmud.product.response.ProductDto;
import me.jobayeralmahmud.product.response.PaginateProduct;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;

    @Override
    public PaginateProduct<ProductDto> getAllProducts(UUID lastId, Pageable pageable) {
        var sliceProduct = productRepository.retrieveAllProducts(lastId, pageable);
        var products = sliceProduct.getContent();

        if (products.isEmpty()) {
            return new PaginateProduct<>(Collections.emptyList(), pageable.getPageSize(), null, false);
        }

        var nextId = sliceProduct.hasNext() ? products.getLast().getId() : null;
        var productDtos = mapProducts(products);

        return new PaginateProduct<>(productDtos, pageable.getPageSize(), nextId, sliceProduct.hasNext());
    }

    @Override
    public Product getProductById(UUID id) {
        return findProductByIdOrThrow(id);
    }

    @Override
    public ProductDto createProduct(CreateProductRequest request) {
        var product = request.toEntity();
        var variants = request.productVariants();

        if (variants == null || variants.isEmpty()) {
            mapMasterVariant(request, product);
        } else {
            ifValueExistThenPerform(variants, CreateProductVariantRequest::toEntity, product::addVariant);
        }

        ifValueExistThenPerform(request.categoryId(), categoryService::getCategoryReference, product::setCategory);
        ifValueExistThenPerform(request.productImages(), CreateProductImageRequest::toEntity, product::addImage);

        return ProductDto.fromEntity(productRepository.save(product));
    }

    @Override
    public Product updateProduct(UUID id, UpdateProductRequest request) {
        var product = findProductByIdOrThrow(id);

        product.setName(request.name());
        product.setDescription(request.description());
        product.setBrand(request.brand());
        product.setImageUrl(request.imageUrl());

        ifValueExistThenPerform(request.categoryId(), categoryService::getCategoryReference, product::setCategory);
        ifValueExistThenPerform(request.productImages(), CreateProductImageRequest::toEntity, product::addImage);
        ifValueExistThenPerform(request.productVariants(), CreateProductVariantRequest::toEntity, product::addVariant);

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(UUID id) {
            productRepository.deleteById(id);
    }

    private Product findProductByIdOrThrow(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("Product not found"));
    }

    private static @NonNull List<ProductDto> mapProducts(List<Product> products) {
        return products.stream()
                .map(ProductDto::fromEntity)
                .toList();
    }

    private <T, R> void ifValueExistThenPerform(T value, Function<T, R> map, Consumer<R> consume) {
        Optional.ofNullable(value).map(map).ifPresent(consume);
    }

    private <T, R> void ifValueExistThenPerform(List<T> list, Function<T, R> map, Consumer<R> consume) {
        Optional.ofNullable(list).ifPresent(
                items -> items.stream().map(map).forEach(consume));
    }

    private static void mapMasterVariant(CreateProductRequest request, Product product) {
        var masterVariant = ProductVariant.builder()
                .variantName("Default Variant")
                .variantValue("Default Value")
                .price(request.basePrice() != null ? request.basePrice() : 0.0)
                .sku(request.baseSku())
                .stockQuantity(request.baseStock() != null ? request.baseStock() : 0)
                .build();
        product.addVariant(masterVariant);
    }
}