package com.erhc.reactiveauthapi.service;

import com.erhc.reactiveauthapi.dto.RegisterDTO;
import com.erhc.reactiveauthapi.exception.EmailAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.erhc.reactiveauthapi.entity.User;
import com.erhc.reactiveauthapi.repository.UserRepository;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {


    private final UserRepository repo;
    private PasswordEncoder passwordEncoder;

    public Mono<User> register(RegisterDTO dto){

        return repo.findByEmail(dto.getEmail())
                .flatMap(existing ->
                        Mono.<User> error(new EmailAlreadyExistsException("User with email already exists!"))
                        )
                .switchIfEmpty(
                        Mono.defer( () -> {
                            User user =  User.builder()
                                    .email(dto.getEmail())
                                    .password(passwordEncoder.encode(dto.getPassword()))
                                    .name(dto.getName())
                                    .role(dto.getRole())
                                    .build();
                            return repo.save(user);
                        })
                );
    }

    public Mono<User> findByEmail(String email){
        return repo.findByEmail(email);
    }

    public Flux<User> findAll(){
        return repo.findAll();
    }

}
