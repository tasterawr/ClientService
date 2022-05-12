package org.loktevik.netcracker.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/sign-in/ata")
    public String wat(){
        String forObject = restTemplate.getForObject("https://vk.com/audios210027871", String.class);
        return forObject;
    }
}
