package me.jobayeralmahmud.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pos_product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    private boolean isPrimary;
    private int sortOrder;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}