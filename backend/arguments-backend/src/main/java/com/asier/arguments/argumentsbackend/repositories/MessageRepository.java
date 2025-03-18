package com.asier.arguments.argumentsbackend.repositories;

import com.asier.arguments.argumentsbackend.entities.Message;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, ObjectId> {
}
