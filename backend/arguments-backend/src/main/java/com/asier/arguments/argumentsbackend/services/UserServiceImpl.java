package com.asier.arguments.argumentsbackend.services;

import com.asier.arguments.argumentsbackend.entities.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.User;
import com.asier.arguments.argumentsbackend.utils.validation.ValidationUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.asier.arguments.argumentsbackend.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<ServiceResponse> insert(com.asier.arguments.argumentsbackend.entities.User entity) {
        if(!ValidationUtils.isValidEntity(entity)) {
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(ServiceResponse.builder().details("Incomplete data").build());
        }
        userRepository.save(entity);
        return ResponseEntity.ok().body(ServiceResponse.builder().details("Success").build());
    }

    @Override
    public ResponseEntity<User> select(ObjectId id) {
        if(id == null){
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
        }
        Optional<User> user = userRepository.findById(id);
        return user.isPresent()? ResponseEntity.ok().body(user.get()) : ResponseEntity.notFound().build();
    }

}
