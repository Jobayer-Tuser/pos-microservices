package me.jobayeralmahmud.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@ConfigurationPropertiesScan
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication(scanBasePackages = "me.jobayeralmahmud")
public class AuthServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}