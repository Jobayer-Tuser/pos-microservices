package me.jobayeralmahmud.auth.config;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.auth.handler.ApplicationAuditAware;
import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    @Bean
    public Faker faker() {
        return new Faker();
    }

    @Bean
    public ApplicationAuditAware auditorAware () {
        return new ApplicationAuditAware();
    }
}
