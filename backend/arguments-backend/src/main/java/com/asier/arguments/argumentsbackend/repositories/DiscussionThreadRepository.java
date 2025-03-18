package com.asier.arguments.argumentsbackend.repositories;

import com.asier.arguments.argumentsbackend.entities.DiscussionThread;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiscussionThreadRepository extends MongoRepository<DiscussionThread, ObjectId> {
}
