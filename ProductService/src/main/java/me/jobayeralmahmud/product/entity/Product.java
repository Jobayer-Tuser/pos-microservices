package me.jobayeralmahmud.product.entity;

import jakarta.persistence.*;
import lombok.*;
import me.jobayeralmahmud.library.utils.Slugify;
import me.jobayeralmahmud.product.request.UpdateProductRequest;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pos_products")
@ToString(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ToString.Include
    private UUID id;

    @ToString.Include
    private UUID storeId;

    @ToString.Include
    private String name;

    @ToString.Include
    private String slug;

    private String description;
    private String imageUrl;

    @ToString.Include
    private String brand;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @ToString.Include
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @ToString.Include
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void generateSlug() {
        this.slug = Slugify.toSlug(name);
    }

    public void addVariant(ProductVariant variant) {
        variants.add(variant);
        variant.setProduct(this);
    }

    public void removeVariant(ProductVariant variant) {
        variants.remove(variant);
        variant.setProduct(null);
    }

    public void addImage(ProductImage image) {
        images.add(image);
        image.setProduct(this);
    }

    public void removeImage(ProductImage image) {
        images.remove(image);
        image.setProduct(null);
    }

    public void update(UpdateProductRequest request) {
        this.name = request.name();
        this.brand = request.brand();
        this.imageUrl = request.imageUrl();
        this.description = request.description();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id != null && id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}