package com.asier.arguments.argumentsbackend.controllers.auth;

import com.asier.arguments.argumentsbackend.entities.user.UserCredentials;
import com.asier.arguments.argumentsbackend.entities.commons.ValidAuthTokens;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.services.auth.components.AuthComponent;
import com.asier.arguments.argumentsbackend.services.auth.ValidAuthsTokenService;
import com.asier.arguments.argumentsbackend.services.auth.CredentialsService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class LoginControllerImpl implements LoginController {
    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private AuthComponent authService;

    @Autowired
    private ValidAuthsTokenService validAuthsTokenService;

    @Override
    public ResponseEntity<ServiceResponse> login(String clientToken, UserCredentials credentials, String remoteIp) {
        if(credentialsService.validate(credentials)){
            String authToken = authService.generateAuthToken(credentials.getUsername());
            ValidAuthTokens token = ValidAuthTokens.toAuthToken(authService.generateAuthToken(credentials.getUsername()));
            token.setIp(remoteIp);
            validAuthsTokenService.insert(token);
            return ResponseEntity.ok(ServiceResponse.builder().status(PropertiesUtils.getProperties(ResourceLocator.STATUS).getProperty("status.done")).result(authToken).build());
        }else{
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(ServiceResponse.builder().status(PropertiesUtils.getProperties(ResourceLocator.STATUS).getProperty("status.invalidCredentials")).build());
        }
    }

    @Override
    public ResponseEntity<ServiceResponse> logout(String clientToken, String authToken) {
        if(validAuthsTokenService.delete(ValidAuthTokens.toAuthToken(authToken))){
            return ResponseEntity.ok(ServiceResponse.builder().status(PropertiesUtils.getProperties(ResourceLocator.STATUS).getProperty("status.done")).build());
        }else{
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(ServiceResponse.builder().status(PropertiesUtils.getProperties(ResourceLocator.STATUS).getProperty("status.unauthorizedAuth")).build());
        }
    }

    @Override
    public String hello(String clientToken, String username) {
        return "hello " + username;
    }
}
