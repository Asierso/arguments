package com.asier.arguments.argumentsbackend.services;

import com.asier.arguments.argumentsbackend.entities.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.User;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ServiceResponse> insert(User entity);
    ResponseEntity<ServiceResponse> select(ObjectId id);
}
