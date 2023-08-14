package com.example.techversantInfoTech.ecommercegateway.filter;

import com.example.techversantInfoTech.ecommercegateway.Jwtutil.JwtService;
import com.example.techversantInfoTech.ecommercegateway.UserRole;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


//This authentication fatewayfilter which simply check autherization head is available or not.If it is not available
//which giver unauthorized response to endpoints which used by this filter.In gatwayconfig we specify the routes
@Component
public class Authentication implements GatewayFilter {

    @Autowired
    RouteValidator routeValidator;


    @Autowired
    JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
      ServerHttpRequest request=exchange.getRequest();
      if(routeValidator.isSecured.test(request)){
          if(authMissing(request)){
              return onError(exchange, HttpStatus.UNAUTHORIZED);
          }
          final String token=request.getHeaders().getOrEmpty("Authorization").get(0);

          Claims claims=jwtService.extractAllClaims(token);
         if(!(claims.get("role").equals(String.valueOf(UserRole.SUPER_ADMIN)))){
             return onError(exchange, HttpStatus.UNAUTHORIZED);
         }

      }
        return chain.filter(exchange);
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response= exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
