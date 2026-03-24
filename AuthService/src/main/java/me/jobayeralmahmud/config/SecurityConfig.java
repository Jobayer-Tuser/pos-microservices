package me.jobayeralmahmud.config;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.enums.UserRole;
import me.jobayeralmahmud.jwt.JwtAuthFilter;
import me.jobayeralmahmud.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

        private final CustomLogoutHandler logoutHandler;
        private final CustomAccessDeniedHandler accessDeniedHandler;
        private final CustomLogoutSuccessHandler logoutSuccessHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter,
                        UserDetailsServiceImpl userDetailsServiceImpl) {
                return http
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(req -> req
                                .requestMatchers(HttpMethod.POST,
                                        "/dev/api/v1/auth/login",
                                        "/dev/api/v1/auth/register",
                                        "/dev/api/v1/auth/token-refresh")
                                .permitAll()
                                .requestMatchers("/api/roles/**").hasRole(UserRole.ADMIN.name())
                                .anyRequest()
                                .authenticated())
                        .userDetailsService(userDetailsServiceImpl)
                        .exceptionHandling(e -> e
                                .accessDeniedHandler(accessDeniedHandler)
                                .authenticationEntryPoint(
                                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                        .sessionManagement(session -> session
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                        .logout(l -> l
                                .logoutUrl("/dev/api/v1/auth/logout")
                                .deleteCookies("refreshToken")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler(logoutSuccessHandler))
                        .build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(12);
        }

        @Bean
        public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                        PasswordEncoder passwordEncoder) {
                var provider = new DaoAuthenticationProvider(userDetailsService);
                provider.setPasswordEncoder(passwordEncoder);
                return new ProviderManager(provider);
        }
}
