package me.jobayeralmahmud.auth.config;

import me.jobayeralmahmud.auth.controller.AuthHandler;
import me.jobayeralmahmud.auth.controller.PermissionSeederHandler;
import me.jobayeralmahmud.auth.controller.RoleSeederHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class AuthRouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> authRoutes(
            AuthHandler authHandler,
            RoleSeederHandler roleSeederHandler,
            PermissionSeederHandler permissionSeederHandler
    ) {
        return RouterFunctions.route()
                .path(Routes.Auth.BASE, builder -> builder
                        .POST(Routes.Auth.LOGIN, authHandler::login)
                        .POST(Routes.Auth.REGISTER, authHandler::register)
                        .POST(Routes.Auth.TOKEN_REFRESH, authHandler::refresh)
                        .GET(Routes.User.VALIDATED_PROFILE, authHandler::getAuthenticatedUserProfile)
                        .POST(Routes.Seed.ROLES, roleSeederHandler::insertRole)
                        .POST(Routes.Seed.PERMISSIONS, permissionSeederHandler::insertPermissions)
                )
                .build();
    }
}
