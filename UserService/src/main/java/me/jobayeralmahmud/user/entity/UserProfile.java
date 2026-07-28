package me.jobayeralmahmud.user.entity;

import jakarta.persistence.*;
import lombok.*;
import me.jobayeralmahmud.library.enums.Gender;
import me.jobayeralmahmud.user.request.UpdateUserProfileRequest;

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

    public void update(UpdateUserProfileRequest request) {
        this.age = request.age();
        this.firstName = request.firstName();
        this.lastName = request.lastName();
        this.displayName = request.displayName();
        this.phoneNumber = request.phoneNumber();
        this.permanentAddress = request.permanentAddress();
        this.permanentPostCode = request.permanentPostCode();
        this.permanentCity = request.permanentCity();
        this.permanentCountry = request.permanentCountry();
        this.invoiceAddress = request.invoiceAddress();
        this.invoicePostCode = request.invoicePostCode();
        this.invoiceCity = request.invoiceCity();
        this.invoiceCountry = request.invoiceCountry();
        this.gender = request.gender();
    }
}