package com.asier.arguments.argumentsbackend.controllers.users;

import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.user.UserCreatorDto;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
