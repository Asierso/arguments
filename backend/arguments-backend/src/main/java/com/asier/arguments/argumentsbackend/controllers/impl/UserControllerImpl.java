package com.asier.arguments.argumentsbackend.controllers.impl;

import com.asier.arguments.argumentsbackend.controllers.UserController;
import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.dtos.UserCreatorDto;
import com.asier.arguments.argumentsbackend.services.AuthService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asier.arguments.argumentsbackend.services.UserService;

@RestController
@RequestMapping("/api/v1")
@SuppressWarnings({"unused"})
public class UserControllerImpl implements UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    
    @Override
    public ResponseEntity<ServiceResponse> insert(String clientToken, UserCreatorDto user) {
        return userService.insert(user);
    }

    @Override
    public ResponseEntity<ServiceResponse> select(String clientToken, ObjectId id) {
        return userService.select(id);
    }

    @Override
    public ResponseEntity<ServiceResponse> delete(String clientToken, ObjectId id) {
        return userService.delete(id);
    }

    @Override
    public ResponseEntity<ServiceResponse> update(String clientToken, ObjectId id, UserCreatorDto user) {
        return userService.update(id,user);
    }

    @Override
    public ResponseEntity<ServiceResponse> findAll(String clientToken) {
        return userService.findAll();
    }

}
