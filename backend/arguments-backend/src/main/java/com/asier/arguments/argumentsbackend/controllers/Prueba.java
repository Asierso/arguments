package com.asier.arguments.argumentsbackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;


@RestController
public class Prueba {
    @GetMapping("/api/token")
    public String test() {
        return "Token: " + PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS_PROPERTIES).getProperty("arguments.api.token");
    }
}
