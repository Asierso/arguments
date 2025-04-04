package com.asier.arguments.argumentsbackend.controllers;

import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
@RequestMapping("/api/v1")
public class AckControllerImpl implements AckController {
    private final Properties props = PropertiesUtils.getProperties(ResourceLocator.STATUS);
    @Override
    public ResponseEntity<ServiceResponse> getArgumentsAck(@RequestParam String clientToken) {
        return ResponseEntity.ok().body(ServiceResponse.builder().status(props.getProperty("status.done")).build());
    }
}
