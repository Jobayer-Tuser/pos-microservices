package me.jobayeralmahmud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final SecurityHeaderInterceptor securityHeaderInterceptor;

    public WebConfig(SecurityHeaderInterceptor securityHeaderInterceptor) {
        this.securityHeaderInterceptor = securityHeaderInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityHeaderInterceptor).addPathPatterns("/dev/api/**");
    }
}
