package com.example.techversantInfoTech.ecommercegateway.filter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//Here we route the request to appropriate microservice and also implement the filter which intercept request and
//authorized and authenticated.
@Configuration
public class GatewayConfig {

    @Autowired
    Authentication filter;



    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("AUTH-SERVICE",r ->r.path("/auth/**")
                        .uri("lb://AUTH-SERVICE"))
                .route("PRODUCT-SERVICE",r ->r.path("/product/**")
                        .filters(f->f.filter(filter))
                        .uri("lb://PRODUCT-SERVICE"))
                .build();
    }
}
