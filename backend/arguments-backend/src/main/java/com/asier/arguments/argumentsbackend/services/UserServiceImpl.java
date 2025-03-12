package com.asier.arguments.argumentsbackend.services;

import com.asier.arguments.argumentsbackend.entities.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.User;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import com.asier.arguments.argumentsbackend.utils.validation.ValidationUtils;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.asier.arguments.argumentsbackend.repositories.UserRepository;

import java.util.Optional;
import java.util.Properties;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    private final Properties statusProps = PropertiesUtils.getProperties(ResourceLocator.STATUS);

    @Override
    public ResponseEntity<ServiceResponse> insert(com.asier.arguments.argumentsbackend.entities.User entity) {
        //Check if annotated fields @Mandatory are filled
        if(!ValidationUtils.isValidEntity(entity)) {
            return ResponseEntity.status(HttpStatusCode.valueOf(400))
                    .body(ServiceResponse.builder().status(statusProps.getProperty("status.incompleteData")).build());
        }

        //Check if there's other user with same username
        if(userRepository.exists(Example.of(User.builder().username(entity.getUsername()).build()))){
            return ResponseEntity.status(HttpStatusCode.valueOf(400))
                    .body(ServiceResponse.builder().status(statusProps.getProperty("status.userAlreadyExists")).build());
        }

        //Hash pwd
        entity.setPassword(BCrypt.hashpw(entity.getPassword(), BCrypt.gensalt()));

        //Save entity and return result ok
        userRepository.save(entity);
        return ResponseEntity.ok().body(ServiceResponse.builder().status(statusProps.getProperty("status.done")).build());
    }

    @Override
    public ResponseEntity<ServiceResponse> select(ObjectId id) {
        //Try to find user
        if(id != null){
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent())
                return ResponseEntity.ok().body(ServiceResponse.builder().status(statusProps.getProperty("status.done")).result(user.get()).build());
        }

        //Return not found
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                .status(statusProps.getProperty("status.notFound")).build());
    }

}
