package com.asier.arguments.argumentsbackend.tasks;

import com.asier.arguments.argumentsbackend.entities.user.User;
import com.asier.arguments.argumentsbackend.entities.user.UserSync;
import com.asier.arguments.argumentsbackend.entities.user.UserCreatorDto;
import com.asier.arguments.argumentsbackend.services.users.UserService;
import com.asier.arguments.argumentsbackend.services.auth.UserSyncService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This cron change "isActive" flag for every user taking reference from sync every 1 min
 */
@Slf4j
@Component
public class UserOnlineSyncTask implements Runnable {
    @Autowired
    private UserService userService;
    @Autowired
    private UserSyncService syncService;
    private final Properties props = PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS);

    //Pagination vars
    private AtomicInteger pageIndex = new AtomicInteger(0);
    private int maxPages = 0;

    @Scheduled(cron = "*/10 * * * * ?")
    public void checkUsers(){
        int enders = Integer.parseInt(props.getProperty("arguments.pools.onlineSyncers"));
        maxPages = userService.findInPage(0).getTotalPages();

        //Prepare threads to process all discussion pages
        try(ExecutorService pool = Executors.newFixedThreadPool(enders)){
            for(int i = 0; i < enders; i++){
                pool.submit(this);
            }

            //Leaves grace time to end and restart index to the next scan
            if(pool.awaitTermination(10, TimeUnit.SECONDS)){
                pool.shutdownNow();
            }
            pageIndex.set(0);
        }catch(InterruptedException e){
            pageIndex.set(0);
        }


    }

    @Override
    public void run() {
        int updatedAmount = 0;
        int page = pageIndex.getAndIncrement();

        //If all pages are processed, stop the thread
        if(page > maxPages){
            Thread.currentThread().interrupt();
        }

        //Get page with delegated index
        Page<User> pageable = userService.findInPage(page);

        int syncedUsers = 0;
        for(User user : userService.findInPage(page)){
            UserSync sync = syncService.select(user.getUsername());
            //Check if user was previously synced at least once
            if(sync==null)
                continue;

            //Update user with sync data
            userService.update(new ObjectId(user.getId()), UserCreatorDto.builder().user(User.builder().isActive(
                            sync.getSync().isAfter(LocalDateTime.now().minusMinutes(1)))
                    .build()).build());

            syncedUsers++;
        }
        log.info("Syncer {}: Synced {} users", Thread.currentThread().getName(), syncedUsers);
    }
}
