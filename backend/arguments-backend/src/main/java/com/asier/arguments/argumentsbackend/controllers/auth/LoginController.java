package com.asier.arguments.argumentsbackend.controllers.auth;

import com.asier.arguments.argumentsbackend.entities.user.UserCredentials;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface LoginController {
    @PostMapping("/login")
    ResponseEntity<ServiceResponse> login(@RequestParam String clientToken, @RequestBody UserCredentials credentials, @RequestAttribute String remoteIp);
    @PostMapping("/auth/hello")
    String hello(@RequestParam String clientToken, @RequestAttribute String username);
    @PostMapping("/auth/logout")
    ResponseEntity<ServiceResponse> logout(@RequestParam String clientToken, @RequestAttribute String authToken);
}
