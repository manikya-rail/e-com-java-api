package com.example.techversantInfoTech.ecommercegateway.filter;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    //Those are the endpoint which open to all users
    //We can specify the open endpoints either in RouteValdator or RouteLocator in GatewayConfig

    public static final List<String> openApiEndpoints = List.of(
            "/auth/register"

    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri->request.getURI().getPath().contains(uri));


}
