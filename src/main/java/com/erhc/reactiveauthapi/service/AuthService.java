package com.erhc.reactiveauthapi.service;

import com.erhc.reactiveauthapi.repository.UserRepository;
import com.erhc.reactiveauthapi.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtUtil jwt;

    public Mono<String> login(String email, String password){
        return repo.findByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")))
                .flatMap(user -> {
                    if(!user.getPassword().equals(password)){
                        return Mono.error(new RuntimeException("Credenciales incorrectas"));
                    }
                    return Mono.just(jwt.generateToken(
                            user.getEmail(),
                            user.getRole()));
                });
    }

}
