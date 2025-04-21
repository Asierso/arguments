package com.asier.arguments.argumentsbackend.services.messaging;

import com.asier.arguments.argumentsbackend.entities.messaging.Message;
import com.asier.arguments.argumentsbackend.repositories.MessageRepository;
import com.asier.arguments.argumentsbackend.services.messaging.processors.MessageQueuing;
import com.asier.arguments.argumentsbackend.utils.annotations.AnnotationsUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageQueuing messageQueuing;
    @Override
    public void insert(Message message) {
        //Add send time to now
        message.setSendTime(Instant.now());
        Message saved = messageRepository.save(message);

        //Enqueue message to generate feedback
        messageQueuing.enqueue(saved);
    }

    @Override
    public boolean update(ObjectId id, Message changes) {
        if(id==null)
            return false;

        Optional<Message> message = messageRepository.findById(id);
        if(message.isPresent()){
            //Change message data with provided changes
            Message messageSource = message.get();
            AnnotationsUtils.modifyEntity(messageSource, changes);
            messageRepository.save(messageSource);
            return true;
        }

        return false;
    }


    @Override
    public Page<Message> findInDiscussion(String discussionId, int page) {
        return null;
    }
}
