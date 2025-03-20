package com.asier.arguments.argumentsbackend.services.auth;

import com.asier.arguments.argumentsbackend.entities.ValidAuthTokens;

import java.util.List;

public interface ValidAuthsTokenService {
    boolean insert(ValidAuthTokens token);
    boolean exists(ValidAuthTokens token);
    boolean delete(ValidAuthTokens token);
    List<ValidAuthTokens> findAll();
}
