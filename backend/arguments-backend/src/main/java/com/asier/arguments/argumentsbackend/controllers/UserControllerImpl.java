package com.asier.arguments.argumentsbackend.controllers;

import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.dtos.UserCreatorDto;
import com.asier.arguments.argumentsbackend.services.AuthService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.asier.arguments.argumentsbackend.services.UserService;

@RestController
@SuppressWarnings({"unused"})
public class UserControllerImpl implements UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    
    @Override
    public ResponseEntity<ServiceResponse> insert(String token, UserCreatorDto user) {
        return userService.insert(user);
    }

    @Override
    public ResponseEntity<ServiceResponse> select(String token, ObjectId id) {
        return userService.select(id);
    }

    @Override
    public ResponseEntity<ServiceResponse> delete(String token, ObjectId id) {
        return userService.delete(id);
    }

    @Override
    public ResponseEntity<ServiceResponse> update(String token, ObjectId id, UserCreatorDto user) {
        return userService.update(id,user);
    }

    @Override
    public ResponseEntity<ServiceResponse> findAll(String token) {
        return userService.findAll();
    }

}
