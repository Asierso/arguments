package com.asier.arguments.argumentsbackend.services;

import com.asier.arguments.argumentsbackend.entities.user.User;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.user.UserCreatorDto;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<ServiceResponse> insert(UserCreatorDto entity);
    User select(ObjectId id);
    User select(String username);
    boolean delete(ObjectId id);
    boolean delete(String username);
    boolean update(ObjectId id, UserCreatorDto entity);
    List<User> findAll();
}
