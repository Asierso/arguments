package com.asier.arguments.argumentsbackend.controllers.messaging;

import com.asier.arguments.argumentsbackend.entities.messaging.Message;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.services.messaging.MessageService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.annotations.AnnotationsUtils;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
@RequestMapping("/api/v1/auth")
public class MessageControllerImpl implements MessageController {
    @Autowired
    private MessageService messageService;

    private final Properties statusProps = PropertiesUtils.getProperties(ResourceLocator.STATUS);

    @Override
    public ResponseEntity<ServiceResponse> insert(String discussionId, String clientToken, Message message) {
        //Add discussion id to message (more RESTful)
        message.setDiscussionId(new ObjectId(discussionId));

        //Check if every param of the message is right
        if(AnnotationsUtils.isNotValidEntity(message)){
            return ResponseEntity.status(400).body(ServiceResponse.builder().status(statusProps.getProperty("status.incompleteData")).build());
        }

        messageService.insert(message);

        return ResponseEntity.ok().body(ServiceResponse.builder()
                .status(statusProps.getProperty("status.done"))
                .build());
    }
}
