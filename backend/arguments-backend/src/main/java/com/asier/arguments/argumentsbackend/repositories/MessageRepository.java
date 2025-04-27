package com.asier.arguments.argumentsbackend.repositories;

import com.asier.arguments.argumentsbackend.entities.messaging.Message;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, ObjectId> {
}
