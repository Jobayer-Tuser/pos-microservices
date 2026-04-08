package me.jobayeralmahmud.product.service;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.exceptions.ResourcesNotFoundException;
import me.jobayeralmahmud.library.response.CursorPageResponse;
import me.jobayeralmahmud.product.entity.Product;
import me.jobayeralmahmud.product.repository.ProductRepository;
import me.jobayeralmahmud.product.request.CreateProductRequest;
import me.jobayeralmahmud.product.request.UpdateProductRequest;
import me.jobayeralmahmud.product.response.ProductDto;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;

    @Override
    public CursorPageResponse<ProductDto> getAllProducts(Long lastId, Pageable pageable) {
        var sliceProduct = productRepository.retrieveAllProducts(lastId, pageable);
        var products = sliceProduct.getContent();

        if (products.isEmpty()) {
            return new CursorPageResponse<>(Collections.emptyList(), pageable.getPageSize(), null, false);
        }

        var nextId = sliceProduct.hasNext() ? products.getLast().getId() : null;
        var productDtos = mapProducts(products);

        return new CursorPageResponse<>(productDtos, pageable.getPageSize(), nextId, sliceProduct.hasNext());
    }

    @Override
    public Product getProductById(Long id) {
        return findProductByIdOrThrow(id);
    }

    @Override
    public ProductDto createProduct(CreateProductRequest request) {
        var product = request.toEntity();

        var category = categoryService.getCategoryReference(request.categoryId());
        Optional.ofNullable(category).ifPresent(product::setCategory);

        Optional.ofNullable(request.productVariants()).ifPresent(variants ->
            variants.forEach(variantRequest ->
                product.addVariant(variantRequest.toEntity())
            )
        );

        Optional.ofNullable(request.productImages()).ifPresent(images ->
            images.forEach(imageRequest ->
                product.addImage(imageRequest.toEntity())
            )
        );

        return ProductDto.fromEntity(productRepository.save(product));
    }

    @Override
    public Product updateProduct(Long id, UpdateProductRequest request) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }

    private Product findProductByIdOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("Product not found"));
    }

    private static @NonNull List<ProductDto> mapProducts(List<Product> products) {
        return products.stream()
                .map(ProductDto::fromEntity)
                .toList();
    }
}