package com.asier.arguments.argumentsbackend.services.messaging;

import com.asier.arguments.argumentsbackend.entities.messaging.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MessageService {
    int insert(Message message);
    boolean update(ObjectId id, Message changes);
    Page<Message> findInDiscussion(ObjectId discussionId, int page);
    List<Message> findInDiscussionByUsername(ObjectId discussionId, String username);
    Message select(ObjectId id);
    boolean delete(ObjectId id);
}
