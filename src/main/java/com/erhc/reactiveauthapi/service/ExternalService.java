package com.erhc.reactiveauthapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
public class ExternalService {

    private final WebClient client = WebClient.create("https://jsonplaceholder.typicode.com");

    public Flux<Map> getPosts(){
        return client.get()
                .uri("/posts")
                .retrieve()
                .bodyToFlux(Map.class);
    }

}
