package aom.finsplit.finsplit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import aom.finsplit.finsplit.entities.User;
import aom.finsplit.finsplit.services.ExternalGreetingApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/greeting")
public class GreetingController {
    @Autowired
    ExternalGreetingApi externalGreetingApi;

    @GetMapping("/{city}")
    public String getGreeting(@AuthenticationPrincipal User user,@PathVariable String city) {
        return externalGreetingApi.getGreeting(city,user.getUsername());
    }
    
    @GetMapping("dub/{message}")
    public String getDubbedFile(@PathVariable String message) throws JsonMappingException, JsonProcessingException {
        externalGreetingApi.generateDub(message);
        return "Dubbed file generated successfully!";
    }
}
