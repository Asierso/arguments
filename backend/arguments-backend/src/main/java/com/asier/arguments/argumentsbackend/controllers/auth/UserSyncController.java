package com.asier.arguments.argumentsbackend.controllers.auth;

import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserSyncController {
    @PostMapping("/sync")
    ResponseEntity<ServiceResponse> userSync(@RequestParam String clientToken, @RequestAttribute String username);
}
