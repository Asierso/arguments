package com.asier.arguments.argumentsbackend.services;

import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.dtos.UserCreatorDto;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ServiceResponse> insert(UserCreatorDto entity);
    ResponseEntity<ServiceResponse> select(ObjectId id);
    ResponseEntity<ServiceResponse> delete(ObjectId id);
    ResponseEntity<ServiceResponse> update(ObjectId id, UserCreatorDto entity);
    ResponseEntity<ServiceResponse> findAll();
}
