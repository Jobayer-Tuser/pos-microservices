package me.jobayeralmahmud.filter;

import me.jobayeralmahmud.service.Jwt;
import me.jobayeralmahmud.service.JwtService;
import org.jspecify.annotations.NullMarked;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtService jwtService;
    private final RouteValidator routeValidator;

    public AuthenticationFilter(RouteValidator routeValidator, JwtService jwtService){
        super(Config.class);
        this.jwtService = jwtService;
        this.routeValidator = routeValidator;
    }

    @Override @NullMarked
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest mutatedRequest = null;
            if (routeValidator.isSecured.test(exchange.getRequest())){
                if(! exchange.getRequest().getHeaders().containsHeader(HttpHeaders.AUTHORIZATION) ){
                    throw new IllegalArgumentException("Header not found");
                }

                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).toString();
                if (authHeader.startsWith("Bearer ")){
                    authHeader = authHeader.replace("Bearer ", "");
                }
                try{
                    Jwt jwt = jwtService.parseToken(authHeader);

                    System.out.println(jwt.getRole());
                    System.out.println(jwt.getUserPermissions());

                    mutatedRequest = exchange.getRequest().mutate()
                            .header("X-User-Role", jwt.getRole())
                            .header("X-User-Permission", jwt.getUserPermissions())
                            .build();

                } catch (Exception e){
                    throw new RuntimeException("Unauthorized access");
                }
            }
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        });
    }

    public static class Config{

    }
}
