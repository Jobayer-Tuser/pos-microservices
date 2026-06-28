package me.jobayeralmahmud.product.config;

import me.jobayeralmahmud.product.controller.CategoryHandler;
import me.jobayeralmahmud.product.controller.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class RouteConfiguration {

    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler productHandler, CategoryHandler categoryHandler) {
        return RouterFunctions.route()
                .path("/dev/api/v1/products", builder -> builder
                        .GET("", productHandler::index)
                        .POST("", productHandler::store)
                        .GET("/{id}", productHandler::show)
                        .PATCH("/{id}", productHandler::update)
                        .DELETE("/{id}", productHandler::destroy)
                )
                .path("/dev/api/v1/categories", builder -> builder
                        .GET("", categoryHandler::index)
                        .POST("", categoryHandler::store)
                        .PATCH("/{id}", categoryHandler::update)
                        .DELETE("/{id}", categoryHandler::destroy)
                )
                .build();
    }
}