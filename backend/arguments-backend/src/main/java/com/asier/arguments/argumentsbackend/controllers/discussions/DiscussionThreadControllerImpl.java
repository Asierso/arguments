package com.asier.arguments.argumentsbackend.controllers.discussions;

import com.asier.arguments.argumentsbackend.entities.DiscussionStatus;
import com.asier.arguments.argumentsbackend.entities.DiscussionThread;
import com.asier.arguments.argumentsbackend.entities.dtos.DiscussionDto;
import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionThread;
import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionDto;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.services.discussions.DiscussionThreadService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.annotations.AnnotationsUtils;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Properties;

@RestController
@RequestMapping("/api/v1/auth")
public class DiscussionThreadControllerImpl implements DiscussionThreadController{
    @Autowired
    private DiscussionThreadService discussionThreadService;
    private final Properties statusProps = PropertiesUtils.getProperties(ResourceLocator.STATUS);
    @Override
    public ResponseEntity<ServiceResponse> insert(String clientToken, DiscussionDto discussionDto, String username) {
        //Check if dto is valid
        if(AnnotationsUtils.isNotValidEntity(discussionDto)){
            return ResponseEntity.status(400).body(ServiceResponse.builder().status(statusProps.getProperty("status.incompleteData")).build());
        }

        //Build discussion with dto data
        DiscussionThread discussion = DiscussionThread.builder()
                .title(discussionDto.getTitle())
                .author(username)
                .createdAt(LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant())
                .maxUsers(discussionDto.getMaxUsers())
                .endAt(LocalDateTime.now().atZone(ZoneOffset.UTC).plusMinutes(discussionDto.getDuration()).toInstant())
                .users(new HashSet<String>())
                .status(DiscussionStatus.STARTED)
                .build();

        discussion.getUsers().add(username);

        //Insert discussion in db
        discussionThreadService.insert(discussion);
        return ResponseEntity.ok().body(ServiceResponse.builder().status(statusProps.getProperty("status.done")).result(discussion).build());
    }

    @Override
    public ResponseEntity<ServiceResponse> findByPage(String clientToken, String page) {
        int pageNum = 0;
        try{
            if(page == null)
                throw new NumberFormatException();

            pageNum = Integer.parseInt(page);
        }catch (NumberFormatException e){
            return ResponseEntity.badRequest().body(ServiceResponse.builder().status(statusProps.getProperty("status.notValidRequest")).build());
        }

        //If page not exists or is impossible number
        if(pageNum < 0){
            return ResponseEntity.status(404).body(ServiceResponse.builder().status(statusProps.getProperty("status.notFound")).build());
        }

        //Get selected page
        Page<DiscussionThread> pag = discussionThreadService.findInPage(pageNum);
        return ResponseEntity.ok().body(ServiceResponse.builder().status(statusProps.getProperty("status.done")).result(pag).build());
    }

    @Override
    public ResponseEntity<ServiceResponse> getDiscussion(String clientToken, String discussionId) {
        DiscussionThread selected = discussionThreadService.select(new ObjectId(discussionId));
        if(selected == null){
            return ResponseEntity.status(404).body(ServiceResponse.builder().status(statusProps.getProperty("status.notFound")).build());
        }
        return ResponseEntity.ok().body(ServiceResponse.builder().status(statusProps.getProperty("status.done")).result(selected).build());
    }
}
