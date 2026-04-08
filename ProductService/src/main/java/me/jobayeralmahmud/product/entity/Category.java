package me.jobayeralmahmud.product.entity;

import jakarta.persistence.*;
import lombok.*;
import me.jobayeralmahmud.library.utils.Slugify;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "pos_product_categories")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long parentId;
    private String name;
    private String description;
    private String slug;

    @OneToMany(mappedBy = "category")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Product> products;

    @PrePersist @PreUpdate
    public void generateSlug() {
        this.slug = Slugify.toSlug(name);
    }
}