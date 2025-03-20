package com.asier.arguments.argumentsbackend.services.auth;

import com.asier.arguments.argumentsbackend.entities.ValidAuthsToken;

import java.util.List;

public interface ValidAuthsTokenService {
    boolean insert(ValidAuthsToken token);
    boolean exists(ValidAuthsToken token);
    boolean delete(ValidAuthsToken token);
    List<ValidAuthsToken> findAll();
}
