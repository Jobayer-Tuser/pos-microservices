package me.jobayeralmahmud.product.repository;

import me.jobayeralmahmud.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT p FROM Product p
            LEFT JOIN FETCH p.category
        where ( :lastId is null or p.id > :lastId )
            order by p.id asc
    """)
    Slice<Product> retrieveAllProducts(@Param("lastId") UUID lastID, Pageable pageable);

    /**
     * The method retrieves a product by its ID, along with its associated category and variants.
     * It uses a JPQL query to perform a left join fetch on the category and variants relationships.
     * This ensures that the related entities are loaded eagerly, preventing the N+1 select problem.
     *
     * @param id The UUID of the product to retrieve.
     * @return An Optional containing the Product if found, or empty if not found.
     */
    Optional<Product> findById(UUID id);

    /**
     * Deletes a product by its unique identifier (UUID).
     *
     * @param id The unique identifier of the product to delete.
     */
    void deleteById(UUID id);
}