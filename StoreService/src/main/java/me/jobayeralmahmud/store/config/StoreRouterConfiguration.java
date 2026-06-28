package me.jobayeralmahmud.store.config;

import me.jobayeralmahmud.store.controller.StoreHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class StoreRouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> storeRoutes(StoreHandler storeHandler) {
        return RouterFunctions.route()
                .path("/dev/api/v1/store", builder -> builder
                        .GET("", storeHandler::index)
                        .POST("", storeHandler::store)
                        .PATCH("/update/{id}", storeHandler::update)
                        .GET("/show/{id}", storeHandler::show)
                )
                .build();
    }
}
