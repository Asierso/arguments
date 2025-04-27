package com.asier.arguments.argumentsbackend.services.messaging.processors;

import com.asier.arguments.argumentsbackend.entities.messaging.Message;
import com.asier.arguments.argumentsbackend.entities.messaging.PaimonMessageDto;
import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionThread;
import com.asier.arguments.argumentsbackend.paimon.PaimonProcessor;
import com.asier.arguments.argumentsbackend.paimon.templates.FeedbackTemplate;
import com.asier.arguments.argumentsbackend.services.discussions.DiscussionThreadService;
import com.asier.arguments.argumentsbackend.services.messaging.MessageService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class MessageQueuing implements Runnable {
    private final ArrayBlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(1000);
    private final ExecutorService pool;
    private final Properties props = PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS);

    @Autowired
    private DiscussionThreadService discussionService;

    @Autowired
    @Lazy
    private MessageService messageService;

    @Autowired
    private PaimonProcessor paimon;

    public MessageQueuing(){
        pool = Executors.newFixedThreadPool(Integer.parseInt(props.getProperty("arguments.pools.queuingThreads")));
    }

    public synchronized void enqueue(Message message){
        messageQueue.add(message);
    }

    @PostConstruct
    public void init(){
        for(int i = 0; i < Integer.parseInt(props.getProperty("arguments.pools.queuingThreads")); i++)
            pool.submit(this);
    }

    /**
     * Executed by every thread in pool. This pool allows to create multiple Paimon requests
     * using a message queue
     */
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try {
                Message message = messageQueue.take();
                log.info("Processing feedback " + message.getId());

                DiscussionThread discussion = discussionService.select(new ObjectId(message.getDiscussionId()));

                if(discussion == null)
                    continue;

                //Generate message for Paimon processor
                PaimonMessageDto paimonMessage = PaimonMessageDto.builder()
                        .discussionId(new ObjectId(discussion.getId()))
                        .topic(discussion.getTitle())
                        .messageId(new ObjectId(message.getId()))
                        .message(message.getMessage())
                        .build();

                //Send request to Paimon using feedback velocity template
                paimon.processAsPrompt(new FeedbackTemplate(paimonMessage),response -> {
                    //Save feedback in the same entity in mongo
                    messageService.update(paimonMessage.getMessageId(),Message.builder()
                            .feedback(response)
                            .build()
                    );
                });

            } catch (InterruptedException e) {
                log.error("Suspended message queuing service at thread {}", Thread.currentThread().getName());
            }
        }
    }
}
