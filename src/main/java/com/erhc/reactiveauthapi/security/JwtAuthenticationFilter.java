package com.erhc.reactiveauthapi.security;

import com.erhc.reactiveauthapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private UserRepository userRepository;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        // Si no hay token, continuar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);
        String email;

        try {
            email = jwtUtil.getEmail(token);
        } catch (Exception e) {
            return chain.filter(exchange); // token inválido
        }

        String  role = jwtUtil.getRole(token);

        // Buscar usuario reactivo
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    UserDetails userDetails = User.builder()
                            .username(user.getEmail())
                            .password("")
                            .roles(role)
                            .build();

                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                }).switchIfEmpty(chain.filter(exchange));
    }
}
