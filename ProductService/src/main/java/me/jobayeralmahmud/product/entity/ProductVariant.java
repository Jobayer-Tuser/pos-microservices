package me.jobayeralmahmud.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import me.jobayeralmahmud.product.enums.ProductStatus;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pos_product_variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String sku;
    private String variantName;
    private String variantValue;
    private double price;
    private double sellPrice;
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Product product;

    @PrePersist
    public void onCreate() {
        if (status == null) {
            status = ProductStatus.IN_STOCK;
        }
    }
}