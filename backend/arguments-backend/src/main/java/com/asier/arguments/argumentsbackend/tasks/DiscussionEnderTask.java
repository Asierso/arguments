package com.asier.arguments.argumentsbackend.tasks;

import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionStatus;
import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionThread;
import com.asier.arguments.argumentsbackend.services.discussions.DiscussionThreadService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class DiscussionEnderTask implements Runnable {
    @Autowired
    private DiscussionThreadService discussionService;
    private final Properties props = PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS);

    //Pagination vars
    private AtomicInteger pageIndex = new AtomicInteger(0);
    private int maxPages = 0;

    @Scheduled(cron = "*/10 * * * * ?")
    public void checkExpiredDiscussions(){
        int enders = Integer.parseInt(props.getProperty("arguments.pools.enders"));
        maxPages = discussionService.findInPage(0).getTotalPages();

        //Prepare threads to process all discussion pages
        try(ExecutorService pool = Executors.newFixedThreadPool(enders)){
            for(int i = 0; i < enders; i++){
                pool.submit(this);
            }

            //Leaves grace time to end and restart index to the next scan
            pool.awaitTermination(10, TimeUnit.SECONDS);
            pool.shutdownNow();
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
        Page<DiscussionThread> pageable = discussionService.findInPage(page);

        //Process every discussion thread in each page
        for(DiscussionThread thread : pageable.toList()){
            Instant now = LocalDateTime.now().toInstant(ZoneOffset.UTC);
            
            //Check if the discussion is expired but still opened and close it
            if(thread.getStatus() == DiscussionStatus.STARTED && thread.getEndAt().isBefore(now)){
                discussionService.alterStatus(new ObjectId(thread.getId()), DiscussionStatus.VOTING);
                updatedAmount++;
            }
        }

        log.info("Pool thread: " + updatedAmount + " discussions updated");
    }
}
