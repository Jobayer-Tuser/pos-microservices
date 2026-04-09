package me.jobayeralmahmud.store.entity;

import jakarta.persistence.*;
import lombok.*;
import me.jobayeralmahmud.library.utils.Slugify;
import me.jobayeralmahmud.store.enums.StoreStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pos_store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID ownerId;
    private String brandName;
    private String slug;
    private String email;
    private String phoneNumber;
    private String logoUrl;
    private String currency;
    private String timezone;
    private Boolean isVerified;
    private String description;
    private String verificationDocumentUrl;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        if (status == null ) {
            status = StoreStatus.INACTIVE;
        }

        if (isVerified == null) {
            isVerified = false;
        }

        slug = Slugify.toSlug(brandName);
    }
}