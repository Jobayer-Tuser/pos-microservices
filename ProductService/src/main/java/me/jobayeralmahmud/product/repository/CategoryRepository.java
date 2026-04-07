package me.jobayeralmahmud.product.repository;

import me.jobayeralmahmud.product.entity.Category;
import me.jobayeralmahmud.product.response.CategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
            select c.id as id,
                 c.name as name,
                 c.slug as slug
            from Category c
        """)
    List<CategoryDto> retrieveAllCategories();
}