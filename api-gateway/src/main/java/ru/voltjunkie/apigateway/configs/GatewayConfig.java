package ru.voltjunkie.apigateway.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {
    @Autowired
    private AuthenticationFilter filter;

    @LoadBalanced
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r->r.path("/api/users/**")
                        .filters(f->f.filter(filter))
                        .uri("lb://user-service"))
                .route("auth-service", r->r.path("/api/auth/**")
                        .filters(f->f.filter(filter))
                        .uri("lb://auth-service"))
                .build();
    }
}
