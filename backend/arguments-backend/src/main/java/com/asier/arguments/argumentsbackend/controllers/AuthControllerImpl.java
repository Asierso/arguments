package com.asier.arguments.argumentsbackend.controllers;

import com.asier.arguments.argumentsbackend.entities.UserCredentials;
import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import com.asier.arguments.argumentsbackend.services.AuthService;
import com.asier.arguments.argumentsbackend.services.CredentialsService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthControllerImpl implements  AuthController{
    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private AuthService authService;

    @Override
    public ResponseEntity<ServiceResponse> login(String token, UserCredentials credentials) {
        log.info("Try" + credentials.getUsername() + ":" + credentials.getPassword());
        if(credentialsService.validate(credentials)){
            return ResponseEntity.ok(ServiceResponse.builder().status(PropertiesUtils.getProperties(ResourceLocator.STATUS).getProperty("status.done")).result(authService.generateAuthToken(credentials.getUsername())).build());
        }else{
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(ServiceResponse.builder().status(PropertiesUtils.getProperties(ResourceLocator.STATUS).getProperty("status.unauthorizedAuth")).build());
        }
    }

    @Override
    public void logout(String token) {

    }

    @Override
    public String hello(String token) {
        return "hello ";
    }
}
