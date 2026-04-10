package me.jobayeralmahmud.product.service;

import me.jobayeralmahmud.product.entity.Product;
import me.jobayeralmahmud.product.request.CreateProductRequest;
import me.jobayeralmahmud.product.request.UpdateProductRequest;
import me.jobayeralmahmud.product.response.ProductDto;
import me.jobayeralmahmud.product.response.PaginateProduct;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    /**
     * Retrieves a paginated list of products.
     *
     * @param lastId   The ID of the last product from the previous page (for pagination).
     * @param pageable The pagination information (page number, page size, sorting).
     * @return A paginated list of ProductDto objects.
     */
    PaginateProduct<ProductDto> getAllProducts(UUID lastId, Pageable pageable);

    /**
     * Creates a new product based on the provided request data.
     *
     * @param request The request object containing the details of the product to be created.
     * @return The created ProductDto object.
     */
    ProductDto createProduct(CreateProductRequest request);

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id The unique identifier of the product to retrieve.
     * @return The Product object corresponding to the provided ID.
     */
    Product getProductById(UUID id);

    /**
     * Updates an existing product with the provided request data.
     *
     * @param id      The unique identifier of the product to update.
     * @param request The request object containing the updated details of the product.
     * @return The updated Product object.
     */
    Product updateProduct(UUID id, UpdateProductRequest request);

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id The unique identifier of the product to delete.
     */
    void deleteProduct(UUID id);
}