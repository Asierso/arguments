package com.asier.arguments.argumentsbackend.controllers.voting;

import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.services.discussions.DiscussionThreadService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Properties;

@RequestMapping("/api/v1/auth")
public class VotingControllerImpl implements VotingController {
    @Autowired
    private DiscussionThreadService discussionService;
    private final Properties statusProps = PropertiesUtils.getProperties(ResourceLocator.STATUS);

    @Override
    public ResponseEntity<ServiceResponse> voteIn(String clientToken, String discussionId, String target, String username) {
        switch (discussionService.voteIn(new ObjectId(discussionId), target)) {
            case 0 -> {
                return ResponseEntity.ok().body(ServiceResponse.builder()
                        .status(statusProps.getProperty("status.done"))
                        .build());
            }
            case 1 -> {
                return ResponseEntity.status(404).body(ServiceResponse.builder()
                        .status(statusProps.getProperty("status.notFound"))
                        .build());
            }
            case 2 -> {
                return ResponseEntity.badRequest().body(ServiceResponse.builder()
                        .status(statusProps.getProperty("status.notMember"))
                        .build());
            }
            case 3 -> {
                return ResponseEntity.status(403).body(ServiceResponse.builder()
                        .status(statusProps.getProperty("status.votingClosed"))
                        .build());
            }
            case 4 -> {
                return ResponseEntity.status(403).body(ServiceResponse.builder()
                        .status(statusProps.getProperty("status.alreadyVoted"))
                        .build());
            }
            default -> {
                return ResponseEntity.badRequest().body(ServiceResponse.builder()
                        .status(statusProps.getProperty("status.notValidRequest"))
                        .build());
            }
        }
    }
}
