package com.asier.arguments.argumentsbackend.controllers;

import java.util.List;

import com.asier.arguments.argumentsbackend.entities.ServiceResponse;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.asier.arguments.argumentsbackend.entities.User;

public interface UserController {
    @PostMapping("/api/v1/user")
    public ResponseEntity<ServiceResponse> insert(@RequestParam String token, @RequestBody User user);

    @GetMapping("/api/v1/user/{id}")
    public ResponseEntity<User> select(@RequestParam String token, @PathVariable ObjectId id);

    @PatchMapping("/api/v1/user")
    public ResponseEntity<Void> update(@RequestAttribute String token);

    @GetMapping("/api/v1/user/all")
    public ResponseEntity<List<User>> findAll(@RequestAttribute String token);
}
