package me.jobayeralmahmud.store.entity;

import jakarta.persistence.*;
import lombok.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ownerId;
    private String brandName;
    private String description;
    private String email;
    private String phoneNumber;
    private String address;
    private String storeType;
    private StoreStatus status;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = StoreStatus.INACTIVE;
        }
    }
}
