package com.asier.arguments.argumentsbackend.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.asier.arguments.argumentsbackend.entities.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

}
