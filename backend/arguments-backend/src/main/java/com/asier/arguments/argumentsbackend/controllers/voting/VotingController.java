package com.asier.arguments.argumentsbackend.controllers.voting;

import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

public interface VotingController {
    @PostMapping("/voting/{discussionId}")
    ResponseEntity<ServiceResponse> voteIn(@RequestParam String clientToken, @PathVariable String discussionId, @RequestAttribute String username);
}
