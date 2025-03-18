package com.asier.arguments.argumentsbackend.controllers;

import com.asier.arguments.argumentsbackend.entities.UserCredentials;
import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface LoginController {
    @PostMapping("/api/v1/login")
    ResponseEntity<ServiceResponse> login(@RequestParam String clientToken, @RequestBody UserCredentials credentials);
    @PostMapping("/api/v1/auth/hello")
    String hello(@RequestParam String clientToken);
    @PostMapping("/api/v1/auth/logout")
    ResponseEntity<ServiceResponse> logout(@RequestParam String clientToken, HttpServletRequest request);
}
