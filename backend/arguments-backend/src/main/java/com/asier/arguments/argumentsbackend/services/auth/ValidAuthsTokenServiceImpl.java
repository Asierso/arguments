package com.asier.arguments.argumentsbackend.services.auth;

import com.asier.arguments.argumentsbackend.entities.ValidAuthsToken;
import com.asier.arguments.argumentsbackend.repositories.ValidAuthsTokenRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValidAuthsTokenServiceImpl implements ValidAuthsTokenService {
    @Autowired
    private ValidAuthsTokenRepository authsRepository;

    @Override
    public boolean insert(ValidAuthsToken token) {
        if(authsRepository.exists(Example.of(token))){
           return false;
        }
        authsRepository.save(token);
        return true;
    }

    @Override
    public boolean exists(ValidAuthsToken token) {
        return authsRepository.exists(Example.of(token)) || authsRepository.findOne(Example.of(ValidAuthsToken.toAuthToken(token.getToken()))).isPresent();
    }

    @Override
    public boolean delete(ValidAuthsToken token) {
        if(token.getId() != null && authsRepository.existsById(new ObjectId(token.getId()))){
            authsRepository.delete(token);
            return true;
        }else{
            Optional<ValidAuthsToken> tokenOpt = authsRepository.findOne(Example.of(token));
            if (tokenOpt.isPresent()) {
                authsRepository.delete(tokenOpt.get());
                return true;
            }
        }
        return false;
    }

    public List<ValidAuthsToken> findAll(){
        return authsRepository.findAll();
    }
}
