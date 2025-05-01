package com.asier.arguments.argumentsbackend.services.users;

import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionThread;
import com.asier.arguments.argumentsbackend.entities.user.UserCredentials;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.user.User;
import com.asier.arguments.argumentsbackend.entities.user.UserCreatorDto;
import com.asier.arguments.argumentsbackend.repositories.UserCredentialsRepository;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import com.asier.arguments.argumentsbackend.utils.annotations.AnnotationsUtils;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.asier.arguments.argumentsbackend.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    private final Properties statusProps = PropertiesUtils.getProperties(ResourceLocator.STATUS);
    private final Properties props = PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS);

    @Override
    public ResponseEntity<ServiceResponse> insert(UserCreatorDto entity) {
        //Check if annotated fields @Mandatory are filled
        if(AnnotationsUtils.isNotValidEntity(entity) ||
                AnnotationsUtils.isNotValidEntity(entity.getUser()) ||
                AnnotationsUtils.isNotValidEntity(entity.getCredentials())) {
            return ResponseEntity.status(HttpStatusCode.valueOf(400))
                    .body(ServiceResponse.builder().status(statusProps.getProperty("status.incompleteData")).build());
        }

        //Check if data is valid
        if(!entity.getUser().getUsername().equals(entity.getCredentials().getUsername())){
            return ResponseEntity.status(HttpStatusCode.valueOf(400))
                    .body(ServiceResponse.builder().status(statusProps.getProperty("status.notValidRequest")).build());
        }

        //Check if there's other user with same username
        if(userRepository.exists(Example.of(User.builder().username(entity.getUser().getUsername()).build()))){
            return ResponseEntity.status(HttpStatusCode.valueOf(400))
                    .body(ServiceResponse.builder().status(statusProps.getProperty("status.userAlreadyExists")).build());
        }

        //Hash pwd
        entity.getCredentials().setPassword(BCrypt.hashpw(entity.getCredentials().getPassword(), BCrypt.gensalt()));

        //Fix @Fix fields data
        AnnotationsUtils.fixEntity(entity);

        //Establish offline by default
        entity.getUser().setIsActive(false);

        //Save entity and return result ok
        userRepository.save(entity.getUser());
        userCredentialsRepository.save(entity.getCredentials());
        return ResponseEntity.ok().body(ServiceResponse.builder().status(statusProps.getProperty("status.done")).build());
    }

    @Override
    public User select(ObjectId id) {
        //Try to find user
        if(id != null){
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent())
                return user.get();
        }
        return null;
    }

    @Override
    public User select(String username) {
        //Try to find user
        if(username != null){
            Optional<User> user = userRepository.findOne(Example.of(User.builder().username(username).isActive(null).build()));
            if(user.isPresent())
                return user.get();
        }
        return null;
    }

    @Override
    public boolean delete(ObjectId id) {
        //Delete user and credentials if exists
        if(id != null){
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent()){
                //Delete credentials if exists
                userCredentialsRepository.findOne(Example.of(UserCredentials.builder()
                                .username(user.get().getUsername()).build()))
                                .ifPresent(val -> userCredentialsRepository.deleteById(new ObjectId(val.getId())));
                //Delete user
                userRepository.deleteById(id);

                //Insert the same cloned user but without data. This is for blocking the creation of another user with same username
                userRepository.save(User.builder()
                                .id(id)
                                .username(user.get().getUsername())
                                .build());

                //Send affirmative answer
                return true;
            }
        }

        //Return not found
        return false;
    }

    @Override
    public boolean delete(String username) {
        if(username != null){
            Optional<User> user = userRepository.findOne(Example.of(User.builder().username(username).build()));
            if(user.isPresent()){
                //Delete credentials if exists
                userCredentialsRepository.findOne(Example.of(UserCredentials.builder()
                                .username(user.get().getUsername()).build()))
                        .ifPresent(val -> userCredentialsRepository.deleteById(new ObjectId(val.getId())));
                //Delete user
                userRepository.deleteById(new ObjectId(user.get().getId()));

                //Insert the same cloned user but without data. This is for blocking the creation of another user with same username
                userRepository.save(User.builder()
                        .id(new ObjectId(user.get().getId()))
                        .username(user.get().getUsername())
                        .build());

                //Send affirmative answer
                return true;
            }
        }
        return false;
    }

    public boolean update(ObjectId id, UserCreatorDto changes){
        if(id==null)
            return false;

        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            //If there are users included in changes, change it
            if(changes.getUser() != null) {
                User userSource = user.get();
                AnnotationsUtils.modifyEntity(userSource, changes.getUser());

                //Fix @Fix fields data
                AnnotationsUtils.fixEntity(userSource);

                userRepository.save(userSource);
            }
            //If there are credentials included in changes, change it
            if(changes.getCredentials() != null) {
                Optional<UserCredentials> credentials = userCredentialsRepository.findOne(Example.of(UserCredentials.builder()
                        .username(user.get().getUsername()).build()));
                if(credentials.isPresent()){
                    //Hash password
                    changes.getCredentials().setPassword(BCrypt.hashpw(changes.getCredentials().getPassword(), BCrypt.gensalt()));

                    //Apply changes
                    UserCredentials credentialsSource = credentials.get();
                    AnnotationsUtils.modifyEntity(credentialsSource,changes.getCredentials());

                    //Fix @Fix fields data
                    AnnotationsUtils.fixEntity(credentialsSource);

                    userCredentialsRepository.save(credentialsSource);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<User> findAll() {
        //Get all users
        return userRepository.findAll();
    }

    @Override
    public Page<User> findInPage(int page) {
        return userRepository.findAll(PageRequest.of(page,Integer.parseInt(props.getProperty("arguments.api.usersPerPage")), Sort.by("username")));
    }

    @Override
    public boolean insertInHistory(String username, DiscussionThread discussion) {
        if (username == null || username.isBlank()) {
            return false;
        }

        //Get user by username
        Optional<User> selected = userRepository.findOne(Example.of(User.builder().username(username).isActive(null).build()));

        //Check if there's no user
        if (selected.isEmpty()) {
            return false;
        }

        //Add discussion to user history
        User user = selected.get();
        user.getHistory().put(new ObjectId(discussion.getId()),discussion.getTitle());
        userRepository.save(user);

        return false;
    }
}
