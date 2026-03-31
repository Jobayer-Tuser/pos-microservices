package me.jobayeralmahmud.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class}, scanBasePackages = {"me.jobayeralmahmud"})
public class StoreServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(StoreServiceApplication.class, args);
    }
}