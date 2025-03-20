package com.asier.arguments.argumentsbackend.controllers;

import java.util.List;

import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.dtos.UserCreatorDto;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.asier.arguments.argumentsbackend.entities.User;

public interface UserController {
    @PostMapping("/users")
    ResponseEntity<ServiceResponse> insert(@RequestParam String clientToken, @RequestBody UserCreatorDto user);

    @GetMapping("/users/id/{id}")
    ResponseEntity<ServiceResponse> selectById(@RequestParam String clientToken, @PathVariable ObjectId id);

    @GetMapping("/users/username/{uname}")
    ResponseEntity<ServiceResponse> selectByUsername(@RequestParam String clientToken, @PathVariable String uname);

    @GetMapping("/users/all")
    ResponseEntity<ServiceResponse> findAll(@RequestParam String clientToken);
}
