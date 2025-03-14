package com.asier.arguments.argumentsbackend.controllers;

import java.util.List;

import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.dtos.UserCreatorDto;
import com.asier.arguments.argumentsbackend.utils.validation.ValidationUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.asier.arguments.argumentsbackend.entities.User;
import com.asier.arguments.argumentsbackend.services.UserService;

@RestController
@SuppressWarnings({"unused"})
public class UserControllerImpl implements UserController {
    @Autowired
    private UserService userService;
    
    @Override
    public ResponseEntity<ServiceResponse> insert(String token, UserCreatorDto user) {
        if(!ValidationUtils.checkToken(token)){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
        return userService.insert(user);
    }

    @Override
    public ResponseEntity<ServiceResponse> select(String token, ObjectId id) {
        if(!ValidationUtils.checkToken(token)){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
        return userService.select(id);
    }

    @Override
    public ResponseEntity<ServiceResponse> delete(String token, ObjectId id) {
        if(!ValidationUtils.checkToken(token)){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
        return userService.delete(id);
    }

    @Override
    public ResponseEntity<ServiceResponse> findAll(String token) {
        if(!ValidationUtils.checkToken(token)){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
        return userService.findAll();
    }

}
