package me.jobayeralmahmud.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import me.jobayeralmahmud.product.enums.ProductStatus;

@Getter
@Setter
@Entity
@Table(name = "pos_product_variants")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String variantName;
    private String variantValue;
    private double price;
    private double sellPrice;
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}