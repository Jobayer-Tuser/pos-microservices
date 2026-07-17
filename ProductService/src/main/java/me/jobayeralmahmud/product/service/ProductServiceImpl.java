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
import me.jobayeralmahmud.product.response.PaginateProduct;
import me.jobayeralmahmud.product.response.ProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;

    @Override
    public PaginateProduct<ProductDto> getAllProducts(UUID lastId, Pageable pageable) {
        var slice = productRepository.retrieveAllProducts(lastId, pageable);
        var products = slice.getContent();
        boolean hasNext = slice.hasNext();

        UUID nextId = hasNext ? products.getLast().getId() : null;

        return new PaginateProduct<>(ProductDto.fromEntity(products), pageable.getPageSize(), nextId, hasNext);
    }

    @Override
    public Product getProductById(UUID id) {
        return findProductByIdOrThrow(id);
    }

    @Override
    @Transactional
    public ProductDto createProduct(CreateProductRequest request) {
        var product = request.toEntity();

        attachCategory(product, request.categoryId());
        attachImages(product, request.productImages());

        if (request.productVariants().isEmpty()) {
            mapMasterVariant(request, product);
        }

        attachVariants(product, request.productVariants());

        return ProductDto.fromEntity(productRepository.save(product));
    }

    @Override
    @Transactional
    public Product updateProduct(UUID id, UpdateProductRequest request) {
        var product = findProductByIdOrThrow(id);

        product.update(request);

        attachCategory(product, request.categoryId());
        attachImages(product, request.productImages());
        attachVariants(product, request.productVariants());

        return product;
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        var product = findProductByIdOrThrow(id);
        productRepository.delete(product);
    }

    private void attachCategory(Product product, UUID categoryId) {
        applyIfPresent(categoryId, categoryService::getCategoryReference, product::setCategory);
    }

    private void attachImages(Product product,  List<CreateProductImageRequest> productImages) {
        mapEach(productImages, CreateProductImageRequest::toEntity, product::addImage);
    }

    private void attachVariants(Product product, List<CreateProductVariantRequest> productVariants) {
        mapEach(productVariants, CreateProductVariantRequest::toEntity, product::addVariant);
    }

    private Product findProductByIdOrThrow(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("Product not found with id: " + id));
    }

    private <T, R> void applyIfPresent(T value, Function<T, R> map, Consumer<R> consume) {
        Optional.ofNullable(value)
                .map(map)
                .ifPresent(consume);
    }

    private <T, R> void mapEach(List<T> list, Function<T, R> map, Consumer<R> consume) {
        Optional.ofNullable(list).ifPresent(
                items -> items.stream()
                        .map(map)
                        .forEach(consume));
    }

    private static void mapMasterVariant(CreateProductRequest request, Product product) {
        var masterVariant = ProductVariant.builder()
                .variantName("Default Variant")
                .variantValue("Default Value")
                .price(Objects.requireNonNullElse(request.basePrice(), 0.0))
                .sku(request.baseSku())
                .stockQuantity(Objects.requireNonNullElse(request.baseStock(), 0))
                .build();
        product.addVariant(masterVariant);
    }
}