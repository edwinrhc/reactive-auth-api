package com.erhc.reactiveauthapi.controller;

import com.erhc.reactiveauthapi.service.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ExternalService externalService;

    @GetMapping("/posts")
    public Flux<Map> getPosts(){
        return externalService.getPosts();
    }
}
