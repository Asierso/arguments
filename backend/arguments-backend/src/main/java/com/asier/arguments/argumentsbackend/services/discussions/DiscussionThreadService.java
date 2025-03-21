package com.asier.arguments.argumentsbackend.services.discussions;

import com.asier.arguments.argumentsbackend.entities.DiscussionThread;
import org.bson.types.ObjectId;

public interface DiscussionThreadService {
    void insert(DiscussionThread discussion);
    DiscussionThread select(ObjectId id);
    boolean delete(ObjectId id);
    boolean update(ObjectId id, DiscussionThread discussion);
}
