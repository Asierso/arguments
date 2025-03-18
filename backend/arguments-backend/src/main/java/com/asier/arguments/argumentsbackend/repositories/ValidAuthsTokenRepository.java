package com.asier.arguments.argumentsbackend.repositories;

import com.asier.arguments.argumentsbackend.entities.ValidAuthsToken;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidAuthsTokenRepository extends MongoRepository<ValidAuthsToken, ObjectId> {
}
