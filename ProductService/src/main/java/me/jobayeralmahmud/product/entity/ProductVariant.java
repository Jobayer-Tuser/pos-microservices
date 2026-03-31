package me.jobayeralmahmud.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pos_product_variants")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String variant_name;
    private String variant_value;
    private double price;
    private double sellPrice;
    private int stockQuantity;
    private String status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}