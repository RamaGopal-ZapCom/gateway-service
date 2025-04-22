package com.zapcom.filter;

import com.zapcom.utill.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/** Created by Rama Gopal Project Name - api-gateway-service */
@Component
public class AuthenticationFilter
    extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
  /*
  private final JwtUtil jwtUtil;

  public AuthenticationFilter(JwtUtil jwtUtil) {
      super(Config.class);
      this.jwtUtil = jwtUtil;
  }

  @Override
  public GatewayFilter apply(Config config) {
      return (exchange, chain) -> {
          ServerHttpRequest request = exchange.getRequest();

          if (request.getURI().getPath().contains("/auth")) {
              return chain.filter(exchange);  // Allow auth endpoints without validation
          }

          if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
              return handleUnauthorizedResponse(exchange);
          }

          String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
          if (authHeader == null || !authHeader.startsWith("Bearer ")) {
              return handleUnauthorizedResponse(exchange);
          }

          String token = authHeader.substring(7);
          if (!jwtUtil.isTokenValid(token)) {
              return handleUnauthorizedResponse(exchange);
          }

          return chain.filter(exchange);
      };
  }

  private Mono<Void> handleUnauthorizedResponse(ServerWebExchange exchange) {
      exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
  }

  public static class Config {
      // Custom configuration if needed
  }*/

  private final JwtUtil jwtUtil;

  public AuthenticationFilter(JwtUtil jwtUtil) {
    super(Config.class);
    this.jwtUtil = jwtUtil;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();

      // Allow requests to /auth/** without authentication
      if (request.getURI().getPath().contains("/auth-service")) {
        return chain.filter(exchange);
      }

      if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
        return handleUnauthorizedResponse(exchange);
      }

      String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return handleUnauthorizedResponse(exchange);
      }

      String token = authHeader.substring(7);
      if (!jwtUtil.isTokenValid(token)) {
        return handleUnauthorizedResponse(exchange);
      }

      return chain.filter(exchange);
    };
  }

  private Mono<Void> handleUnauthorizedResponse(ServerWebExchange exchange) {
    exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
    return exchange.getResponse().setComplete();
  }

  public static class Config {
    // Custom configuration if needed
  }
}
