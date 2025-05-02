package com.asier.arguments.argumentsbackend.services.users;

import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionThread;
import com.asier.arguments.argumentsbackend.entities.user.User;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.user.UserCreatorDto;
import com.asier.arguments.argumentsbackend.services.TaskPagination;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService extends TaskPagination<User> {
    ResponseEntity<ServiceResponse> insert(UserCreatorDto entity);
    User select(ObjectId id);
    User select(String username);
    boolean delete(ObjectId id);
    boolean delete(String username);
    boolean update(ObjectId id, UserCreatorDto entity);
    List<User> findAll();
    Page<User> findInPage(int page);
    void insertInHistory(String username, DiscussionThread discussion);
    boolean exists(ObjectId id);
    boolean exists(String username);
}
