package me.jobayeralmahmud.user.entity;

import jakarta.persistence.*;
import lombok.*;
import me.jobayeralmahmud.library.enums.Gender;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "pos_user_profiles")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID userId;
    private int age;
    private String firstName;
    private String lastName;
    private String displayName;
    private String phoneNumber;
    private String permanentAddress;
    private String permanentPostCode;
    private String permanentCity;
    private String permanentCountry;
    private String invoiceAddress;
    private String invoicePostCode;
    private String invoiceCity;
    private String invoiceCountry;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;
}
