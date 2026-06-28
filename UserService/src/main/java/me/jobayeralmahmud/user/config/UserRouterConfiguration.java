package me.jobayeralmahmud.user.config;

import me.jobayeralmahmud.user.controller.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class UserRouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        return RouterFunctions.route()
                .path(Routes.USER_SERVICE, builder -> builder
                        .GET("", userHandler::index)
                        .POST("", userHandler::store)
                        .PATCH(Routes.UPDATE_USER_PROFILE + "/{id}", userHandler::update)
                        .GET("/role", userHandler::confirmRole)
                        .GET("/permissions", userHandler::confirmPermission)
                )
                .build();
    }
}
