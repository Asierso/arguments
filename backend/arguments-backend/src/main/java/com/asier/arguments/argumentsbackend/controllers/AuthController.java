package com.asier.arguments.argumentsbackend.controllers;

import com.asier.arguments.argumentsbackend.entities.UserCredentials;
import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface AuthController {
    @PostMapping("/api/v1/login")
    ResponseEntity<ServiceResponse> login(@RequestParam String token, @RequestBody UserCredentials credentials);
    @PostMapping("/api/v1/auth/hello")
    String hello(@RequestParam String token);
    @PostMapping("/api/v1/auth/logout")
    void logout(@RequestParam String token);
}
