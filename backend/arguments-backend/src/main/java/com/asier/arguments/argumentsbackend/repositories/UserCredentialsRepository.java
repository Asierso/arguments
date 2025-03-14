package com.asier.arguments.argumentsbackend.repositories;

import com.asier.arguments.argumentsbackend.entities.UserCredentials;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserCredentialsRepository extends MongoRepository<UserCredentials, ObjectId> {
}
