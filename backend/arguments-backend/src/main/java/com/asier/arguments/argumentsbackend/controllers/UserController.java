package com.asier.arguments.argumentsbackend.controllers;

import java.util.List;

import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.dtos.UserCreatorDto;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.asier.arguments.argumentsbackend.entities.User;

public interface UserController {
    @PostMapping("/api/v1/users")
    ResponseEntity<ServiceResponse> insert(@RequestParam String clientToken, @RequestBody UserCreatorDto user);

    @GetMapping("/api/v1/users/{id}")
    ResponseEntity<ServiceResponse> select(@RequestParam String clientToken, @PathVariable ObjectId id);

    @DeleteMapping("/api/v1/users/{id}")
    ResponseEntity<ServiceResponse> delete(@RequestParam String clientToken,@PathVariable ObjectId id);

    @PatchMapping("/api/v1/users/{id}")
    ResponseEntity<ServiceResponse> update(@RequestParam String clientToken,@PathVariable ObjectId id, @RequestBody UserCreatorDto user);

    @GetMapping("/api/v1/users/all")
    ResponseEntity<ServiceResponse> findAll(@RequestParam String clientToken);
}
