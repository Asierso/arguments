package com.asier.arguments.argumentsbackend.services.discussions.processors;

import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionThread;
import com.asier.arguments.argumentsbackend.entities.discussion.PaimonDiscussionThreadDto;
import com.asier.arguments.argumentsbackend.entities.messaging.Message;
import com.asier.arguments.argumentsbackend.paimon.PaimonProcessor;
import com.asier.arguments.argumentsbackend.paimon.templates.VotingTemplate;
import com.asier.arguments.argumentsbackend.services.discussions.DiscussionMembersService;
import com.asier.arguments.argumentsbackend.services.messaging.MessageService;
import com.asier.arguments.argumentsbackend.utils.PooledQueue;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class PaimonDiscussionQueuing extends PooledQueue<DiscussionThread> {
    @Autowired
    @Lazy
    private MessageService messageService;
    @Autowired
    private PaimonProcessor paimon;
    @Autowired
    private DiscussionMembersService membersService;

    private final Properties props = PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS);
    public PaimonDiscussionQueuing(){
        super(Integer.parseInt(PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS).getProperty("arguments.pools.queuingThreads")));
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                DiscussionThread discussion = takeFromQueue();
                log.info("Processing ia vote " + discussion.getId());

                int threads = Integer.parseInt(props.getProperty("arguments.pools.votingComposerThreads"));

                ArrayList<Message> messageBuffer = new ArrayList<>();
                Page<Message> paginationIndex = messageService.findInDiscussion(new ObjectId(discussion.getId()),0);
                AtomicInteger index = new AtomicInteger(0);

                //Launch pool to get all message pages by page
                ExecutorService pool = Executors.newFixedThreadPool(threads);
                try{
                    for(int i = 0; i < threads; i++){
                        pool.submit(() -> {
                            bufferMessage(index, paginationIndex.getTotalPages(),new ObjectId(discussion.getId()),messageBuffer);
                        });
                    }
                }finally {
                    pool.shutdown();
                    try {
                        if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                            pool.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        pool.shutdownNow();
                    }
                }

                HashMap<String,List<String>> messages = new HashMap<>();

                //Filter messages by sender and order it by send time asc
                for (String username : discussion.getUsers()){
                    messages.put(username,messageBuffer
                            .stream()
                            .filter(o -> o.getSender().equals(username))
                            .sorted(Comparator.comparing(Message::getSendTime))
                            .map(Message::getMessage)
                            .toList());
                }

                //Group messages by sender
                PaimonDiscussionThreadDto paimonDiscussion = PaimonDiscussionThreadDto.builder()
                        .discussionId(new ObjectId(discussion.getId()))
                        .topic(discussion.getTitle())
                        .messages(messages)
                        .build();

                paimon.processAsPrompt(new VotingTemplate(paimonDiscussion),response -> {
                    String replace = response.trim().toLowerCase().replace("\"", "");
                    if(discussion.getUsers().contains(replace)){
                        membersService.votePaimonIn(new ObjectId(discussion.getId()), replace);
                    }else{
                        //TODO: Can't vote
                    }
                });

            }catch (InterruptedException e) {
                log.error("Suspended discussion queuing service at thread {}", Thread.currentThread().getName());
            }
        }
    }

    public void bufferMessage(AtomicInteger index, int maxPage, ObjectId discussion, ArrayList<Message> messageBuffer){
        if(index.get() <= maxPage){
            Page<Message> messages = messageService.findInDiscussion(discussion,index.getAndIncrement());
            messageBuffer.addAll(messages.toList());
        }
    }
}
