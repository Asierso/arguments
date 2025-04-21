package com.asier.arguments.argumentsbackend.repositories;

import com.asier.arguments.argumentsbackend.entities.user.UserCredentials;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialsRepository extends MongoRepository<UserCredentials, ObjectId> {
}
