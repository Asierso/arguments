package com.asier.arguments.argumentsbackend.controllers;

import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface AckController {
    @GetMapping("/ack")
    ResponseEntity<ServiceResponse> getArgumentsAck(@RequestParam String clientToken);
}
