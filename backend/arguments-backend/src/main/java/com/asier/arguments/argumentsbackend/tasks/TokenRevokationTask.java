package com.asier.arguments.argumentsbackend.tasks;

import com.asier.arguments.argumentsbackend.entities.user.UserCredentials;
import com.asier.arguments.argumentsbackend.entities.commons.ValidAuthTokens;
import com.asier.arguments.argumentsbackend.services.auth.components.AuthComponent;
import com.asier.arguments.argumentsbackend.services.auth.CredentialsService;
import com.asier.arguments.argumentsbackend.services.auth.ValidAuthsTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This cron deletes every 1 min the tokens of the deleted users
 */
@Slf4j
@Component
public class TokenRevokationTask {
    @Autowired
    private ValidAuthsTokenService validAuthsTokenService;
    @Autowired
    private CredentialsService credentialsService;
    @Autowired
    private AuthComponent authService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkTokens(){
        List<String> userList = credentialsService.findAll().stream().map(UserCredentials::getUsername).toList();
        int unused = 0;
        for(ValidAuthTokens token : validAuthsTokenService.findAll()){
           String username = authService.getAuthSubject(token.getToken());
           if(username == null){
               continue;
           }
           if(!userList.contains(username)){
               validAuthsTokenService.delete(token);
               unused++;
           }
        }
        log.info("Deleted {} tokens from list", unused);
    }
}
