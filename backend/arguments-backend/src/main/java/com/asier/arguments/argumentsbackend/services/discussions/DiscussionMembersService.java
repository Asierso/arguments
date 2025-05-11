package com.asier.arguments.argumentsbackend.services.discussions;

import com.asier.arguments.argumentsbackend.entities.user.User;
import org.bson.types.ObjectId;

import java.util.HashSet;

public interface DiscussionMembersService {
    int join(ObjectId id, String username);
    int voteIn(ObjectId id, String target, String actor);
    int votePaimonIn(ObjectId id, String target);
    HashSet<User> getUsers(ObjectId id);
}
