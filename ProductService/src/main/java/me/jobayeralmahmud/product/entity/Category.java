package me.jobayeralmahmud.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import me.jobayeralmahmud.library.utils.Slugify;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "pos_product_categories")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID parentId;
    private String name;
    private String description;
    private String slug;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Product> products;

    @PrePersist @PreUpdate
    public void generateSlug() {
        this.slug = Slugify.toSlug(name);
    }
}