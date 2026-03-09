package me.jobayeralmahmud.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Search criteria for filtering users.
 * Can be extended with additional fields as needed.
 */
@Data
@Builder
public class UserSearchCriteria {

    private String email;
    private String firstName;
    private String lastName;
    private Long roleId;
    private Boolean emailVerified;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
}