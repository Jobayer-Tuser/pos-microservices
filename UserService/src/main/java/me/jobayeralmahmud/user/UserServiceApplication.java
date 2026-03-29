package me.jobayeralmahmud.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(
        exclude = { UserDetailsServiceAutoConfiguration.class },
        scanBasePackages = {"me.jobayeralmahmud" }
)
public class UserServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
