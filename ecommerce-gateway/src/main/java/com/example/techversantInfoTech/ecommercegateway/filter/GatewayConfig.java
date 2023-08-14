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
                .route("CLIENT-SERVICE",r ->r.path("/api/auth/client/register")
                        .filters(f->f.filter(filter))
                        .uri("lb://AUTH-SERVICE"))
                .route("ALLCLIENTS-SERVICE",r ->r.path("/api/auth/client")
                        .filters(f->f.filter(filter))
                        .uri("lb://AUTH-SERVICE"))
                .route("EDIT-SERVICE",r->r.path("/api/auth/client/edit/**")
                        .uri("lb://AUTH-SERVICE"))
                .route("USERBYID-SERVICE",r->r.path("/api/auth/client/**")
                        .uri("lb://AUTH-SERVICE"))

                .route("AUTH-SERVICE",r ->r.path("/api/auth/**")
                        .uri("lb://AUTH-SERVICE"))

                .build();
    }
}
