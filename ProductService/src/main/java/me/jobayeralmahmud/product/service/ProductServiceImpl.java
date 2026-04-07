package me.jobayeralmahmud.product.service;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.product.entity.Product;
import me.jobayeralmahmud.product.repository.ProductRepository;
import me.jobayeralmahmud.product.request.CreateProductRequest;
import me.jobayeralmahmud.product.request.UpdateProductRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;

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

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, UpdateProductRequest request) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }
}