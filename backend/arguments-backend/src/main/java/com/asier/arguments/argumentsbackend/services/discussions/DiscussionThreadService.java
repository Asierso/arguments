package com.asier.arguments.argumentsbackend.services.discussions;

import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionStatus;
import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionThread;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface DiscussionThreadService {
    void insert(DiscussionThread discussion);
    DiscussionThread select(ObjectId id);
    Page<DiscussionThread> findInPage(int page);
    boolean delete(ObjectId id);
    boolean update(ObjectId id, DiscussionThread discussion);
    int join(ObjectId id, String username);
    boolean alterStatus(ObjectId id, DiscussionStatus status);
}