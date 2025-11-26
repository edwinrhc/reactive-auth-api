package com.erhc.reactiveauthapi.controller;

import com.erhc.reactiveauthapi.dto.RegisterDTO;
import com.erhc.reactiveauthapi.entity.User;
import com.erhc.reactiveauthapi.service.AuthService;
import com.erhc.reactiveauthapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Mono<User> register(@Valid @RequestBody RegisterDTO dto){
        return userService.register(dto);
    }

    @PostMapping("/login")
    public Mono<Map<String,String>> login(@RequestBody Map<String,String> body){
        return authService.login(body.get("email"), body.get("password"))
                .map(token -> Map.of("token",token));
    }

    @GetMapping("/list")
    public Flux<User> listUsers(){
        return userService.findAll();
    }



}
