package com.asier.arguments.argumentsbackend.services.messaging.processors;

import com.asier.arguments.argumentsbackend.entities.messaging.Message;
import com.asier.arguments.argumentsbackend.entities.messaging.PaimonMessageDto;
import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionThread;
import com.asier.arguments.argumentsbackend.paimon.PaimonProcessor;
import com.asier.arguments.argumentsbackend.paimon.templates.FeedbackTemplate;
import com.asier.arguments.argumentsbackend.services.discussions.DiscussionThreadService;
import com.asier.arguments.argumentsbackend.services.messaging.MessageService;
import com.asier.arguments.argumentsbackend.utils.PooledQueue;
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
public class PaimonMessageQueuing extends PooledQueue<Message> {
    @Autowired
    private DiscussionThreadService discussionService;
    @Autowired
    @Lazy
    private MessageService messageService;
    @Autowired
    private PaimonProcessor paimon;

    public PaimonMessageQueuing(){
        super(Integer.parseInt(PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS).getProperty("arguments.pools.queuingThreads")));
    }

    /**
     * Executed by every thread in pool. This pool allows to create multiple Paimon requests
     * using a message queue
     */
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try {
                Message message = takeFromQueue();
                log.info("Processing feedback " + message.getId());
                log.info("Debugging PAIMON: Message is: " + message + "\n");

                DiscussionThread discussion = discussionService.select(new ObjectId(message.getDiscussionId()));
                log.info("Debugging PAIMON: Discussio is: " + discussion + "\n");
                
                if(discussion == null)
                    continue;

                //If message is garbage, just leave feedback generation
                if(message.getMessage().length() < 20){
                    messageService.update(new ObjectId(message.getId()),Message.builder()
                            .feedback("Unknown")
                            .build());
                    continue;
                }

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
                            .feedback(response.trim())
                            .build()
                    );
                });

            } catch (InterruptedException e) {
                log.error("Suspended message queuing service at thread {}", Thread.currentThread().getName());
            }
        }
    }
}
