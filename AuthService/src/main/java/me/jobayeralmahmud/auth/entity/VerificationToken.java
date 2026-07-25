package me.jobayeralmahmud.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pos_verification_token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String tokenType;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime verifiedAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate()
    {
        createdAt = LocalDateTime.now();
        expiredAt = LocalDateTime.now().plus(Duration.ofMinutes(15));
    }

    public void verifyToken(LocalDateTime now)
    {
        if (verifiedAt != null) {
            throw new IllegalArgumentException("Token already verified!");
        }

        if (expiredAt.isBefore(now)) {
            throw new IllegalArgumentException("This Token is expired please request for new token!");
        }

        verifiedAt = now;
    }
}