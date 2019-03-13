package com.fengwenyi.springbootapireactiveweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author Wenyi Feng
 * @since 2019-03-12
 */
@Configuration
@EnableWebFlux
public class RoutingConfiguration {

    @Autowired
    private UserHandler handler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        /*return RouterFunctions
                .route(RequestPredicates.GET("/s5/get/{id}")
                        .and(RequestPredicates
                                .accept(MediaType.APPLICATION_JSON_UTF8)), requestHandler::extraResult)
                .andRoute(RequestPredicates.GET("/s5/list")
                        .and(RequestPredicates
                                .accept(MediaType.APPLICATION_JSON_UTF8)), requestHandler::listResult)
                .andRoute(RequestPredicates.POST("/s5/put/")
                        .and(RequestPredicates
                                .accept(MediaType.APPLICATION_JSON_UTF8)), requestHandler::createView);*/
        return RouterFunctions
                .route(RequestPredicates.POST("/user/save")
                        .and(RequestPredicates
                                .contentType(MediaType.APPLICATION_JSON_UTF8)), handler::savePerson);
    }

}
