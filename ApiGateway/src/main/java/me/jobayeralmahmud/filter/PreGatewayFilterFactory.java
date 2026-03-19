package me.jobayeralmahmud.filter;

import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.exceptions.BearerTokenException;
import me.jobayeralmahmud.service.Jwt;
import me.jobayeralmahmud.service.JwtService;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Gateway filter factory for pre-processing requests with JWT authentication.
 * Validates secured routes, extracts and verifies Bearer tokens, and adds user info to request headers.
 */
@Slf4j
@Component
public class PreGatewayFilterFactory extends AbstractGatewayFilterFactory<PreGatewayFilterFactory.Config> {

    private final JwtService jwtService;
    private final RouteValidator routeValidator;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String X_USER_ID = "X-User-Id";
    private static final String X_USER_ROLE = "X-User-Role";
    private static final String X_USER_PERMISSIONS = "X-User-Permissions";

    public PreGatewayFilterFactory(RouteValidator routeValidator, JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
        this.routeValidator = routeValidator;
    }

    /**
     * Applies the filter to validate JWT and mutate the request with user information.
     */
    @Override
    public @NullMarked GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if (!routeValidator.isSecured.test(exchange.getRequest())) {
                return chain.filter(exchange);
            }

            var headers = exchange.getRequest().getHeaders();
            if(!headers.containsHeader(HttpHeaders.AUTHORIZATION)) {
                throw new BearerTokenException("Missing Authorization Header");
            }

            var authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.trim().startsWith(BEARER_PREFIX)){
                throw new BearerTokenException("Missing Bearer Token or Invalid token format");
            }

            var token = authHeader.trim().replace(BEARER_PREFIX, "");

            return extractTokenAndMutateRequestWithUserInfo(exchange, chain, token);
        };
    }

    /**
     * Extracts the JWT token, parses it, and mutates the request with user role and permissions.
     */
    private @NonNull Mono<Void> extractTokenAndMutateRequestWithUserInfo(
            ServerWebExchange exchange, GatewayFilterChain chain, String token) {

        Jwt jwt             = jwtService.parseToken(token);
        var role            = jwt.getRole();
        var userId          = String.valueOf(jwt.getUserId());
        System.out.println(userId);
        var permissions     = jwt.getUserPermissions();
        var requestBuilder  = exchange.getRequest().mutate();

        if (role != null && !role.isEmpty()) {
            requestBuilder.header(X_USER_ROLE, role);
        }
        if (permissions != null && !permissions.isEmpty()) {
            requestBuilder.header(X_USER_PERMISSIONS, permissions);
        }

        if (userId != null && !userId.isEmpty()) {
            requestBuilder.header(X_USER_ID, userId);
        }

        var mutatedRequest = requestBuilder.build();
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    /**
     * Configuration class for the filter factory.
     */
    public static class Config {

    }
}
