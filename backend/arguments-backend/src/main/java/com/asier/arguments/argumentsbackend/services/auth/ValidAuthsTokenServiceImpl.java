package com.asier.arguments.argumentsbackend.services.auth;

import com.asier.arguments.argumentsbackend.entities.ValidAuthTokens;
import com.asier.arguments.argumentsbackend.repositories.ValidAuthTokensRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValidAuthsTokenServiceImpl implements ValidAuthsTokenService {
    @Autowired
    private ValidAuthTokensRepository authsRepository;

    @Override
    public boolean insert(ValidAuthTokens token) {
        if(authsRepository.exists(Example.of(token))){
           return false;
        }
        authsRepository.save(token);
        return true;
    }

    @Override
    public boolean exists(ValidAuthTokens token) {
        return authsRepository.exists(Example.of(token)) || authsRepository.findOne(Example.of(ValidAuthTokens.toAuthToken(token.getToken()))).isPresent();
    }

    @Override
    public boolean delete(ValidAuthTokens token) {
        if(token.getId() != null && authsRepository.existsById(new ObjectId(token.getId()))){
            authsRepository.delete(token);
            return true;
        }else{
            Optional<ValidAuthTokens> tokenOpt = authsRepository.findOne(Example.of(token));
            if (tokenOpt.isPresent()) {
                authsRepository.delete(tokenOpt.get());
                return true;
            }
        }
        return false;
    }

    public List<ValidAuthTokens> findAll(){
        return authsRepository.findAll();
    }
}
