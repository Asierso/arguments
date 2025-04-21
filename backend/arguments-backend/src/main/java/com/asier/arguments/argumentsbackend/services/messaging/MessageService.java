package com.asier.arguments.argumentsbackend.services.messaging;

import com.asier.arguments.argumentsbackend.entities.messaging.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface MessageService {
    int insert(Message message);
    boolean update(ObjectId id, Message changes);
    Page<Message> findInDiscussion(String discussionId, int page);
}
