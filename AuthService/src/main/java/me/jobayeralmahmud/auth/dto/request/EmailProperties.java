package me.jobayeralmahmud.auth.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spring.mail")
public record EmailProperties(
        @Valid Smtp smtp,
        @Min(0) @Max(1000) int port,
        @NotBlank @Email String from
) {

    public record Smtp(
            @NotBlank String host,
            @Min(1) @Max(65535) int port
    ) {}
}
