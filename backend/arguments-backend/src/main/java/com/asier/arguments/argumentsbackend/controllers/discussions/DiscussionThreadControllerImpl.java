package com.asier.arguments.argumentsbackend.controllers.discussions;

import com.asier.arguments.argumentsbackend.entities.DiscussionThread;
import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import com.asier.arguments.argumentsbackend.services.discussions.DiscussionThreadService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.annotations.AnnotationsUtils;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Properties;

@RestController
@RequestMapping("/api/v1/auth")
public class DiscussionThreadControllerImpl implements DiscussionThreadController{
    @Autowired
    private DiscussionThreadService discussionThreadService;
    private final Properties statusProps = PropertiesUtils.getProperties(ResourceLocator.STATUS);
    @Override
    public ResponseEntity<ServiceResponse> insert(String clientToken, DiscussionThread discussion, String username) {
        //Check if dto is valid
        if(AnnotationsUtils.isNotValidEntity(discussion)){
            return ResponseEntity.status(400).body(ServiceResponse.builder().status(statusProps.getProperty("status.incompleteData")).build());
        }

        //Add discussion data
        discussion.setAuthor(username);
        discussion.setCreatedAt(LocalDateTime.now());

        //Insert discussion in db
        discussionThreadService.insert(discussion);
        return ResponseEntity.ok().body(ServiceResponse.builder().status(statusProps.getProperty("status.done")).build());
    }

    @Override
    public void findByPage(String clientToken, int page) {

    }
}
