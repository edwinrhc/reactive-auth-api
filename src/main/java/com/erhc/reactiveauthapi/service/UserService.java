package com.erhc.reactiveauthapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.erhc.reactiveauthapi.entity.User;
import com.erhc.reactiveauthapi.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public Mono<User> register(User user){
        return repo.findByEmail(user.getEmail())
                .flatMap(existing ->
                        Mono.<User> error(new RuntimeException("User with email already exists!"))
                        )
                .switchIfEmpty(repo.save(user));
    }

    public Mono<User> findByEmail(String email){
        return repo.findByEmail(email);
    }

    public Flux<User> findAll(){
        return repo.findAll();
    }

}
