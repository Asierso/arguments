package com.asier.arguments.argumentsbackend.services.messaging;

import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionStatus;
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
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
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
    @Autowired
    private MongoTemplate mongoTemplate;

    private final Properties props = PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS);

    @Override
    public int insert(Message message) {
        //Add send time to now
        message.setSendTime(Instant.now());

        //Check if message body is empty
        if(message.getMessage().trim().isBlank()){
            return 4;
        }

        //Before save, check if the target discussion is even valid and not ended
        DiscussionThread discussion = discussionService.select(new ObjectId(message.getDiscussionId()));
        if(discussion == null){
            return 1;
        }

        //Check if discussion is expired
        if(message.getSendTime().isAfter(discussion.getEndAt()) || discussion.getStatus() != DiscussionStatus.STARTED){
            return 2;
        }

        //User should be joined before send a message
        if(!discussion.getUsers().contains(message.getSender())){
            return 3;
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
    public Page<Message> findInDiscussion(ObjectId discussionId, int page) {
        int limit = Integer.parseInt(props.getProperty("arguments.api.messagesPerPage"));

        //Build query to get all messages in same discussion ordered by most recent
        Query messagesInDiscussion = new Query().addCriteria(
                Criteria.where("discussionId").is(discussionId)
        ).with(PageRequest.of(page,limit, Sort.by("sendTime").descending()));

        long total = mongoTemplate.count(Query.of(messagesInDiscussion).limit(-1).skip(-1), Message.class);

        //Get messages filtered and paginated and return with pagination metadata
        List<Message> messages = mongoTemplate.find(messagesInDiscussion,Message.class);

        return new PageImpl<>(messages,PageRequest.of(page,limit),total);
    }

    @Override
    public Message select(ObjectId id) {
        Optional<Message> selected = messageRepository.findById(id);
        return selected.orElse(null);
    }

    @Override
    public boolean delete(ObjectId id) {
        Optional<Message> selected = messageRepository.findById(id);

        //Check if message doesn't exist (send 404)
        if(selected.isEmpty())
            return false;

        //Remove message
        messageRepository.deleteById(id);
        return true;
    }
}
