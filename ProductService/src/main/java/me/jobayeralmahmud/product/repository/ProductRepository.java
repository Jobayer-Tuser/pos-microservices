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

    Optional<Product> findById(UUID id);
}