package com.asier.arguments.argumentsbackend.controllers.messaging;

import com.asier.arguments.argumentsbackend.entities.messaging.Message;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface MessageController {
    @PostMapping("/messaging/{discussionId}")
    ResponseEntity<ServiceResponse> insert(@PathVariable String discussionId, @RequestParam String clientToken, @RequestBody Message message);
    @GetMapping("/messaging/{discussionId}")
    ResponseEntity<ServiceResponse> findByPage(@PathVariable String discussionId, @RequestParam String clientToken, @RequestParam(defaultValue = "0") String page);
}
