package com.asier.arguments.argumentsbackend.services.auth;

import com.asier.arguments.argumentsbackend.entities.user.UserCredentials;

import java.util.List;

public interface CredentialsService {
    boolean validate(UserCredentials credentials);
    List<UserCredentials> findAll();
}
