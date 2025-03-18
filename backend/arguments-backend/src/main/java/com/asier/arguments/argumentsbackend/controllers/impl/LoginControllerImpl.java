package com.asier.arguments.argumentsbackend.controllers.impl;

import com.asier.arguments.argumentsbackend.controllers.LoginController;
import com.asier.arguments.argumentsbackend.entities.UserCredentials;
import com.asier.arguments.argumentsbackend.entities.ValidAuthsToken;
import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import com.asier.arguments.argumentsbackend.services.AuthService;
import com.asier.arguments.argumentsbackend.services.ValidAuthsTokenService;
import com.asier.arguments.argumentsbackend.services.CredentialsService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginControllerImpl implements LoginController {
    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ValidAuthsTokenService validAuthsTokenService;

    @Override
    public ResponseEntity<ServiceResponse> login(String clientToken, UserCredentials credentials) {
        if(credentialsService.validate(credentials)){
            String authToken = authService.generateAuthToken(credentials.getUsername());
            validAuthsTokenService.insert(ValidAuthsToken.toAuthToken(authService.generateAuthToken(credentials.getUsername())));
            return ResponseEntity.ok(ServiceResponse.builder().status(PropertiesUtils.getProperties(ResourceLocator.STATUS).getProperty("status.done")).result(authToken).build());
        }else{
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(ServiceResponse.builder().status(PropertiesUtils.getProperties(ResourceLocator.STATUS).getProperty("status.unauthorizedAuth")).build());
        }
    }

    @Override
    public ResponseEntity<ServiceResponse> logout(String clientToken, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(ServiceResponse.builder().status(PropertiesUtils.getProperties(ResourceLocator.STATUS).getProperty("status.notValidRequest")).build());
        }

        if(validAuthsTokenService.delete(ValidAuthsToken.toAuthToken(authHeader.substring(7)))){
            return ResponseEntity.ok(ServiceResponse.builder().status(PropertiesUtils.getProperties(ResourceLocator.STATUS).getProperty("status.done")).build());
        }else{
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(ServiceResponse.builder().status(PropertiesUtils.getProperties(ResourceLocator.STATUS).getProperty("status.unauthorizedAuth")).build());
        }
    }

    @Override
    public String hello(String clientToken) {
        return "hello ";
    }
}
