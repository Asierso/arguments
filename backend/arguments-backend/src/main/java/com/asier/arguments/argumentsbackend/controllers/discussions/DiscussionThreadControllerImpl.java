package com.asier.arguments.argumentsbackend.controllers.discussions;

import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionStatus;
import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionThread;
import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionDto;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.services.discussions.DiscussionMembersService;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

@RestController
@RequestMapping("/api/v1/auth")
public class DiscussionThreadControllerImpl implements DiscussionThreadController{
    @Autowired
    private DiscussionThreadService discussionThreadService;
    @Autowired
    private DiscussionMembersService discussionMembersService;
    private final Properties statusProps = PropertiesUtils.getProperties(ResourceLocator.STATUS);
    private final Properties props = PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS);
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
                .users(new HashSet<>())
                .votes(new HashMap<>())
                .voteCache(new HashSet<>())
                .status(DiscussionStatus.STARTED)
                .votingGraceAt(LocalDateTime.now().atZone(ZoneOffset.UTC).plusMinutes(discussionDto.getDuration()).plusMinutes(Integer.parseInt(props.getProperty("arguments.ln.votingGrace"))).toInstant())
                .build();

        discussion.getUsers().add(username);
        discussion.getVotes().put(username,0);

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

    @Override
    public ResponseEntity<ServiceResponse> join(String clientToken, String discussionId, String username) {
        switch (discussionMembersService.join(new ObjectId(discussionId),username)){
            case 0 -> {
                return ResponseEntity.ok().body(ServiceResponse.builder().status(statusProps.getProperty("status.done")).build());
            }
            case 1 -> {
                return ResponseEntity.status(404).body(ServiceResponse.builder().status(statusProps.getProperty("status.notFound")).build());
            }
            case 2 -> {
                return ResponseEntity.badRequest().body(ServiceResponse.builder().status(statusProps.getProperty("status.expiredDiscussion")).build());
            }
            case 3 -> {
                return ResponseEntity.status(409).body(ServiceResponse.builder().status(statusProps.getProperty("status.discussionMaxReached")).build());
            }
            case 4 -> {
                return ResponseEntity.badRequest().body(ServiceResponse.builder().status(statusProps.getProperty("status.userAlreadyExists")).build());
            }
            default -> {
                return ResponseEntity.badRequest().body(ServiceResponse.builder().status(statusProps.getProperty("status.notValidRequest")).build());
            }
        }
    }
}
