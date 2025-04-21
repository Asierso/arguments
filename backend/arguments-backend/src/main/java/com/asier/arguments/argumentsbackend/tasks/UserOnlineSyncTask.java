package com.asier.arguments.argumentsbackend.tasks;

import com.asier.arguments.argumentsbackend.entities.user.User;
import com.asier.arguments.argumentsbackend.entities.user.UserSync;
import com.asier.arguments.argumentsbackend.entities.user.UserCreatorDto;
import com.asier.arguments.argumentsbackend.services.UserService;
import com.asier.arguments.argumentsbackend.services.auth.UserSyncService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * This cron change "isActive" flag for every user taking reference from sync every 1 min
 */
@Slf4j
@Component
public class UserOnlineSyncTask {
    @Autowired
    private UserService userService;
    @Autowired
    private UserSyncService syncService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkUsers(){
        int unused = 0;
        for(User user : userService.findAll()){
            UserSync sync = syncService.select(user.getUsername());
            //Check if user was previously synced at least once
            if(sync==null)
                continue;

            //Update user with sync data
            userService.update(new ObjectId(user.getId()), UserCreatorDto.builder().user(User.builder().isActive(
                    sync.getSync().isAfter(LocalDateTime.now().minusMinutes(1)))
                    .build()).build());

            unused++;
        }
        log.info("Synced {} users", unused);
    }
}
