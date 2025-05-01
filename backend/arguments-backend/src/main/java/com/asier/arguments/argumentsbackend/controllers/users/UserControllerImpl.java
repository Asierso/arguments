package com.asier.arguments.argumentsbackend.controllers.users;

import com.asier.arguments.argumentsbackend.entities.user.User;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.user.UserCreatorDto;
import com.asier.arguments.argumentsbackend.services.auth.AuthService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asier.arguments.argumentsbackend.services.users.UserService;

import java.util.Properties;
@RestController
@RequestMapping("/api/v1")
@SuppressWarnings({"unused"})
public class UserControllerImpl implements UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    private final Properties statusProps = PropertiesUtils.getProperties(ResourceLocator.STATUS);
    
    @Override
    public ResponseEntity<ServiceResponse> insert(String clientToken, UserCreatorDto user) {
        return userService.insert(user);
    }

    @Override
    public ResponseEntity<ServiceResponse> selectById(String clientToken, ObjectId id) {
        User user = userService.select(id);
        if(user != null){
            return ResponseEntity.ok().body(ServiceResponse.builder()
                    .status(statusProps.getProperty("status.done"))
                    .result(user).build());
        }

        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                .status(statusProps.getProperty("status.notFound")).build());
    }

    @Override
    public ResponseEntity<ServiceResponse> selectByUsername(String clientToken, String uname) {
        User selected = userService.select(uname);
        if(selected != null){
            return ResponseEntity.ok().body(ServiceResponse.builder()
                    .status(statusProps.getProperty("status.done"))
                    .result(selected).build());
        }

        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                .status(statusProps.getProperty("status.notFound")).build());
    }

    @Override
    public ResponseEntity<ServiceResponse> findAll(String clientToken) {
        return ResponseEntity.ok().body(ServiceResponse.builder()
                .status(statusProps.getProperty("status.done"))
                .result(userService.findAll()).build());
    }

}
