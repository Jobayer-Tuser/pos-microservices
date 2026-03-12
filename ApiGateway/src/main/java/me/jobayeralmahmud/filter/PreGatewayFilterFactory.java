package me.jobayeralmahmud.filter;

import io.jsonwebtoken.JwtException;
import me.jobayeralmahmud.service.Jwt;
import me.jobayeralmahmud.service.JwtService;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class PreGatewayFilterFactory extends AbstractGatewayFilterFactory<PreGatewayFilterFactory.Config> {

    private final JwtService jwtService;
    private final RouteValidator routeValidator;

    public PreGatewayFilterFactory(RouteValidator routeValidator, JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
        this.routeValidator = routeValidator;
    }

    @Override
    public @NullMarked GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if (routeValidator.isSecured.test(exchange.getRequest())) {
                return chain.filter(exchange);
            }

            var headers = exchange.getRequest().getHeaders();
            if(!headers.containsHeader(HttpHeaders.AUTHORIZATION)) {
                return onError( exchange,"Missing Authorization Header", HttpStatus.UNAUTHORIZED);
            }

            var authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")){
                return onError( exchange,"Missing Bearer Token or Invalid token format", HttpStatus.UNAUTHORIZED);
            }

            var token = authHeader.replace("Bearer ", "");

            return extractTokenAndMutateRequestWithUserInfo(exchange, chain, token);
        };
    }

    private @NonNull Mono<Void> extractTokenAndMutateRequestWithUserInfo(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        try {
            Jwt jwt = jwtService.parseToken(token);

            var mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Role", jwt.getRole())
                    .header("X-User-Permissions", jwt.getUserPermissions())
                    .build();
            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (JwtException e) {
            return onError(exchange, "Invalid Bearer Token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus status) {
        var response =  exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    public static class Config {

    }
}
