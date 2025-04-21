package com.asier.arguments.argumentsbackend.controllers.messaging;

import com.asier.arguments.argumentsbackend.entities.messaging.Message;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface MessageController {
    @PostMapping("/messaging/{discussionId}")
    ResponseEntity<ServiceResponse> insert(@PathVariable String discussionId, @RequestParam String clientToken, @RequestBody Message message);
}
