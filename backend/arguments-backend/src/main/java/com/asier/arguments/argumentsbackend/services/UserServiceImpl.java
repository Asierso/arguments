package com.asier.arguments.argumentsbackend.services;

import com.asier.arguments.argumentsbackend.entities.UserCredentials;
import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.User;
import com.asier.arguments.argumentsbackend.entities.dtos.UserCreatorDto;
import com.asier.arguments.argumentsbackend.repositories.UserCredentialsRepository;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import com.asier.arguments.argumentsbackend.utils.annotations.AnnotationsUtils;
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

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    private final Properties statusProps = PropertiesUtils.getProperties(ResourceLocator.STATUS);

    @Override
    public ResponseEntity<ServiceResponse> insert(UserCreatorDto entity) {
        //Check if annotated fields @Mandatory are filled
        if(!AnnotationsUtils.isValidEntity(entity) ||
                !AnnotationsUtils.isValidEntity(entity.getUser()) ||
                !AnnotationsUtils.isValidEntity(entity.getCredentials())) {
            return ResponseEntity.status(HttpStatusCode.valueOf(400))
                    .body(ServiceResponse.builder().status(statusProps.getProperty("status.incompleteData")).build());
        }

        //Check if data is valid
        if(!entity.getUser().getUsername().equals(entity.getCredentials().getUsername())){
            return ResponseEntity.status(HttpStatusCode.valueOf(400))
                    .body(ServiceResponse.builder().status(statusProps.getProperty("status.notValidRequest")).build());
        }

        //Check if there's other user with same username
        if(userRepository.exists(Example.of(User.builder().username(entity.getUser().getUsername()).build()))){
            return ResponseEntity.status(HttpStatusCode.valueOf(400))
                    .body(ServiceResponse.builder().status(statusProps.getProperty("status.userAlreadyExists")).build());
        }

        //Hash pwd
        entity.getCredentials().setPassword(BCrypt.hashpw(entity.getCredentials().getPassword(), BCrypt.gensalt()));

        //Save entity and return result ok
        userRepository.save(entity.getUser());
        userCredentialsRepository.save(entity.getCredentials());
        return ResponseEntity.ok().body(ServiceResponse.builder().status(statusProps.getProperty("status.done")).build());
    }

    @Override
    public ResponseEntity<ServiceResponse> select(ObjectId id) {
        //Try to find user
        if(id != null){
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent())
                return ResponseEntity.ok().body(ServiceResponse.builder()
                        .status(statusProps.getProperty("status.done"))
                        .result(user.get()).build());
        }

        //Return not found
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                .status(statusProps.getProperty("status.notFound")).build());
    }

    @Override
    public ResponseEntity<ServiceResponse> delete(ObjectId id) {
        //Delete user and credentials if exists
        if(id != null){
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent()){
                //Delete credentials if exists
                userCredentialsRepository.findOne(Example.of(UserCredentials.builder()
                                .username(user.get().getUsername()).build()))
                                .ifPresent(val -> userCredentialsRepository.deleteById(new ObjectId(val.getId())));
                //Delete user
                userRepository.deleteById(id);

                //Send affirmative answer
                return ResponseEntity.ok().body(ServiceResponse.builder()
                        .status(statusProps.getProperty("status.done"))
                        .build());
            }
        }

        //Return not found
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                .status(statusProps.getProperty("status.notFound")).build());
    }

    @Override
    public ResponseEntity<ServiceResponse> update(ObjectId id,UserCreatorDto changes) {
        if(id != null){
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent()){
                User source = user.get();
                AnnotationsUtils.modifyEntity(source,changes.getUser());
                userRepository.save(source);

                //Send affirmative answer
                return ResponseEntity.ok().body(ServiceResponse.builder()
                        .status(statusProps.getProperty("status.done"))
                        .build());
            }
        }

        //Return not found
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                .status(statusProps.getProperty("status.notFound")).build());
    }

    @Override
    public ResponseEntity<ServiceResponse> findAll() {
        return ResponseEntity.ok().body(ServiceResponse.builder()
                .status(statusProps.getProperty("status.done"))
                .result(userRepository.findAll()).build());
    }

}
