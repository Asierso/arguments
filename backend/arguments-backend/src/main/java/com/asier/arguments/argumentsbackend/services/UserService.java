package com.asier.arguments.argumentsbackend.services;

import com.asier.arguments.argumentsbackend.entities.User;
import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.dtos.UserCreatorDto;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<ServiceResponse> insert(UserCreatorDto entity);
    User select(ObjectId id);
    User select(String username);
    boolean delete(ObjectId id);
    boolean delete(String username);
    ResponseEntity<ServiceResponse> update(ObjectId id, UserCreatorDto entity);
    List<User> findAll();
}
