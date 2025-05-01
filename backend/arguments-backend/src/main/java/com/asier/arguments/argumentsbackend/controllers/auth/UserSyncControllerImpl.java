package com.asier.arguments.argumentsbackend.controllers.auth;

import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.services.auth.UserSyncService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
@RequestMapping("/api/v1/auth")
public class UserSyncControllerImpl implements UserSyncController{
    @Autowired
    private UserSyncService syncService;
    private final Properties statusProps = PropertiesUtils.getProperties(ResourceLocator.STATUS);
    @Override
    public ResponseEntity<ServiceResponse> userSync(String clientToken, String username) {
        syncService.update(username);
        return ResponseEntity.ok().body(ServiceResponse.builder()
                .status(statusProps.getProperty("status.done"))
                .build());
    }
}
