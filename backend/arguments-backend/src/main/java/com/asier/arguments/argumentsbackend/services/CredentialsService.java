package com.asier.arguments.argumentsbackend.services;

import com.asier.arguments.argumentsbackend.entities.UserCredentials;

public interface CredentialsService {
    boolean validate(UserCredentials credentials);
}
