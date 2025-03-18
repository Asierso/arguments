package com.asier.arguments.argumentsbackend.services;

import com.asier.arguments.argumentsbackend.entities.ValidAuthsToken;

public interface ValidAuthsTokenService {
    boolean insert(ValidAuthsToken token);
    boolean exists(ValidAuthsToken token);
    boolean delete(ValidAuthsToken token);
}
