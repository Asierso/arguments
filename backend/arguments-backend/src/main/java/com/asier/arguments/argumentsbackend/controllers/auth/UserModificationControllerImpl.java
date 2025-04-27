package com.asier.arguments.argumentsbackend.controllers.auth;

import com.asier.arguments.argumentsbackend.entities.user.User;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.user.UserCreatorDto;
import com.asier.arguments.argumentsbackend.services.users.UserService;
import com.asier.arguments.argumentsbackend.services.auth.AuthService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class UserModificationControllerImpl implements UserModificationController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    private final Properties statusProps = PropertiesUtils.getProperties(ResourceLocator.STATUS);

    @Override
    public ResponseEntity<ServiceResponse> deleteById(String clientToken, ObjectId id, String username) {
        User selected = userService.select(id);

        //Check if the user that wants to delete and user is the same
        if (selected != null && username.equals(selected.getUsername())) {
            //Try to delete
            boolean action = userService.delete(id);

            return action ?
                    ResponseEntity.ok().body(ServiceResponse.builder()
                            .status(statusProps.getProperty("status.done"))
                            .build()) :
                    ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                            .status(statusProps.getProperty("status.notFound"))
                            .build());
        }

        if (selected == null) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                    .status(statusProps.getProperty("status.notFound"))
                    .build());
        }

        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                .status(statusProps.getProperty("status.unauthorizedAuth"))
                .build());
    }

    @Override
    public ResponseEntity<ServiceResponse> deleteByUsername(String clientToken, String uname, String username) {
        //Check if the user that wants to delete and user is the same
        if (username.equals(uname)) {
            //Try to delete
            return userService.delete(uname) ? ResponseEntity.ok().body(ServiceResponse.builder()
                    .status(statusProps.getProperty("status.done"))
                    .build()) :
                    ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                            .status(statusProps.getProperty("status.notFound"))
                            .build());
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                .status(statusProps.getProperty("status.unauthorizedAuth"))
                .build());
    }

    @Override
    public ResponseEntity<ServiceResponse> updateById(String clientToken, ObjectId id, UserCreatorDto user, String username) {
        User selected = userService.select(id);

        //Check if the user that wants to update and user is the same
        if (selected != null && username.equals(selected.getUsername())) {
            return userService.update(id, user)?
                    ResponseEntity.ok().body(ServiceResponse.builder()
                            .status(statusProps.getProperty("status.done"))
                            .build())
                    :
                    ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                            .status(statusProps.getProperty("status.notFound"))
                            .build());
        }

        if (selected == null) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                    .status(statusProps.getProperty("status.notFound"))
                    .build());
        }

        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                .status(statusProps.getProperty("status.unauthorizedAuth"))
                .build());
    }

    @Override
    public ResponseEntity<ServiceResponse> updateByUsername(String clientToken, String uname, UserCreatorDto user, String username) {
        User selected = userService.select(uname);

        //Check if the user that wants to update and user is the same
        if (selected != null && username.equals(uname)) {
            return userService.update(new ObjectId(selected.getId()), user) ?
                    ResponseEntity.ok().body(ServiceResponse.builder()
                            .status(statusProps.getProperty("status.done"))
                            .build())
                    :
                    ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                            .status(statusProps.getProperty("status.notFound"))
                            .build());
        }

        if (selected == null) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                    .status(statusProps.getProperty("status.notFound"))
                    .build());
        }

        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(ServiceResponse.builder()
                .status(statusProps.getProperty("status.unauthorizedAuth"))
                .build());
    }
}
