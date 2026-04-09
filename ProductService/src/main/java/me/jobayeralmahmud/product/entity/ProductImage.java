package me.jobayeralmahmud.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pos_product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String imageUrl;
    private boolean isPrimary;
    private int sortOrder;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Product product;
}