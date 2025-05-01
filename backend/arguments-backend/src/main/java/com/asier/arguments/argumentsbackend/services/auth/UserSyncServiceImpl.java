package com.asier.arguments.argumentsbackend.services.auth;

import com.asier.arguments.argumentsbackend.entities.user.UserSync;
import com.asier.arguments.argumentsbackend.repositories.UserSyncRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserSyncServiceImpl implements UserSyncService{
    @Autowired
    private UserSyncRepository syncRepository;

    @Override
    public void update(String username) {
        Optional<UserSync> selected = syncRepository.findOne(Example.of(UserSync.builder().username(username).build()));
        if(selected.isPresent()){
            selected.get().setSync(LocalDateTime.now());
            syncRepository.save(selected.get());
        }else{
            syncRepository.save(UserSync.builder().username(username).sync(LocalDateTime.now()).build());
        }
    }

    @Override
    public UserSync select(String username) {
        Optional<UserSync> selected = syncRepository.findOne(Example.of(UserSync.builder().username(username).build()));
        return selected.orElse(null);
    }
}
