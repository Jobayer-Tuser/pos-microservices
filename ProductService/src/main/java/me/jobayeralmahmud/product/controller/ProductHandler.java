package me.jobayeralmahmud.product.controller;

import jakarta.servlet.ServletException;
import jakarta.validation.Validator;
import me.jobayeralmahmud.library.controller.BaseHandler;
import me.jobayeralmahmud.product.request.CreateProductRequest;
import me.jobayeralmahmud.product.request.UpdateProductRequest;
import me.jobayeralmahmud.product.response.ProductDto;
import me.jobayeralmahmud.product.response.PaginateProduct;
import me.jobayeralmahmud.product.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.util.UUID;

@Component
public class ProductHandler extends BaseHandler {

    private final ProductService productService;

    public ProductHandler(Validator validator, ProductService productService) {
        super(validator);
        this.productService = productService;
    }

    public ServerResponse index(ServerRequest request) {
        UUID lastId = request.param("lastId").map(UUID::fromString).orElse(null);
        Pageable pageable = getPageable(request);
        PaginateProduct<ProductDto> products = productService.getAllProducts(lastId, pageable);
        return ok(products, "Products retrieved successfully");
    }

    public ServerResponse store(ServerRequest request) throws ServletException, IOException {
        CreateProductRequest createRequest = request.body(CreateProductRequest.class);
        validate(createRequest);
        return created(productService.createProduct(createRequest), "Product created successfully");
    }

    public ServerResponse show(ServerRequest request) {
        UUID productId = getUUIDPathVariable(request, "id");
        return ok(productService.getProductById(productId), "Products retrieved successfully");
    }

    public ServerResponse update(ServerRequest request) throws ServletException, IOException {
        UUID id = getUUIDPathVariable(request, "id");
        UpdateProductRequest updateRequest = request.body(UpdateProductRequest.class);
        validate(updateRequest);
        return ok(productService.updateProduct(id, updateRequest), "Product updated successfully");
    }

    public ServerResponse destroy(ServerRequest request) {
        UUID id = getUUIDPathVariable(request, "id");
        productService.deleteProduct(id);
        return noContent("Product deleted successfully");
    }
}
