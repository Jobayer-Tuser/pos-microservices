package me.jobayeralmahmud.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndPoints = List.of(
            Routes.LOGIN,
            Routes.REGISTER,
            Routes.TOKEN_REFRESH,
            Routes.EUREKA_SERVER
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndPoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}


/**
 *  "/dev/api/v1/auth/login",
 *             "/dev/api/v1/auth/register",
 *             "/dev/api/v1/auth/token-refresh",
 *             "/eureka"
 */