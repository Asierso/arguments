package com.asier.arguments.argumentsbackend.services;

import com.asier.arguments.argumentsbackend.entities.UserCredentials;
import com.asier.arguments.argumentsbackend.repositories.UserCredentialsRepository;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CredentialsServiceImpl implements CredentialsService {
    @Autowired
    private UserCredentialsRepository credentialsRepository;
    @Override
    public boolean validate(UserCredentials credentials) {
        Optional<UserCredentials> opt =  credentialsRepository.findOne(Example.of(UserCredentials.builder()
                .username(credentials.getUsername()).build()));

        if(opt.isPresent()){
            return BCrypt.checkpw(credentials.getPassword(),opt.get().getPassword());
        }

        return false;

    }
}
