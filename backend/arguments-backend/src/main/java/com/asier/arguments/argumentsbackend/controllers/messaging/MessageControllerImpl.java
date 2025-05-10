package com.asier.arguments.argumentsbackend.controllers.messaging;

import com.asier.arguments.argumentsbackend.entities.messaging.Message;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.services.messaging.MessageService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.annotations.AnnotationsUtils;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

        //Try to insert message and return the result of insertion
        switch(messageService.insert(message)){
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
                        .status(statusProps.getProperty("status.expiredDiscussion"))
                        .build());
            }
            case 3 -> {
                return ResponseEntity.badRequest().body(ServiceResponse.builder()
                        .status(statusProps.getProperty("status.notMember"))
                        .build());
            }
            default -> {
                return ResponseEntity.badRequest().body(ServiceResponse.builder()
                        .status(statusProps.getProperty("status.notValidRequest"))
                        .build());
            }
        }
    }

    @Override
    public ResponseEntity<ServiceResponse> findByPage(String discussionId, String clientToken, String page) {
        //Check if page num is correct. Messages must be paginated to optimize speed
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

        //Get paginated messages
        Page<Message> pag = messageService.findInDiscussion(new ObjectId(discussionId),pageNum);
        return ResponseEntity.ok().body(ServiceResponse.builder().status(statusProps.getProperty("status.done")).result(pag).build());

    }

    @Override
    public ResponseEntity<ServiceResponse> findByPageByUsername(String discussionId, String username, String clientToken) {
        //If discussion id is null return not valid request
        if(discussionId == null){
            return  ResponseEntity.badRequest().body(ServiceResponse.builder().status(statusProps.getProperty("status.notValidRequest")).build());
        }

        //Get all messages by discussion and by username
        List<Message> messages = messageService.findInDiscussionByUsername(new ObjectId(discussionId),username);

        //If messages list is null, return not found error
        if(messages == null){
            return ResponseEntity.status(404).body(ServiceResponse.builder().status(statusProps.getProperty("status.notFound")).build());
        }

        return ResponseEntity.ok().body(ServiceResponse.builder().status(statusProps.getProperty("status.done")).result(messages).build());

    }
}
