package com.asier.arguments.argumentsbackend.services.messaging;

import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionThread;
import com.asier.arguments.argumentsbackend.entities.messaging.Message;
import com.asier.arguments.argumentsbackend.repositories.MessageRepository;
import com.asier.arguments.argumentsbackend.services.discussions.DiscussionThreadService;
import com.asier.arguments.argumentsbackend.services.messaging.processors.MessageQueuing;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.annotations.AnnotationsUtils;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Properties;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageQueuing messageQueuing;
    @Autowired
    private DiscussionThreadService discussionService;

    @Override
    public int insert(Message message) {
        //Add send time to now
        message.setSendTime(Instant.now());

        //Before save, check if the target discussion is even valid and not ended
        DiscussionThread discussion = discussionService.select(message.getDiscussionId());
        if(discussion == null){
            return 1;
        }
        if(message.getSendTime().isAfter(discussion.getEndAt())){
            return 2;
        }

        Message saved = messageRepository.save(message);

        //Enqueue message to generate feedback
        messageQueuing.enqueue(saved);

        return 0;
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
