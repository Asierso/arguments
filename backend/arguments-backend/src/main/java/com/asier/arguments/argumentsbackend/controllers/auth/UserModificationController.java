package com.asier.arguments.argumentsbackend.controllers.auth;

import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.user.UserCreatorDto;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface UserModificationController {
    @DeleteMapping("/users/id/{id}")
    ResponseEntity<ServiceResponse> deleteById(@RequestParam String clientToken, @PathVariable ObjectId id, @RequestAttribute String username);
    @DeleteMapping("/users/username/{uname}")
    ResponseEntity<ServiceResponse> deleteByUsername(@RequestParam String clientToken, @PathVariable String uname, @RequestAttribute String username);
    @PatchMapping("/users/id/{id}")
    ResponseEntity<ServiceResponse> updateById(@RequestParam String clientToken,@PathVariable ObjectId id, @RequestBody UserCreatorDto user, @RequestAttribute String username);
    @PatchMapping("/users/username/{uname}")
    ResponseEntity<ServiceResponse> updateByUsername(@RequestParam String clientToken,@PathVariable String uname, @RequestBody UserCreatorDto user, @RequestAttribute String username);
}
