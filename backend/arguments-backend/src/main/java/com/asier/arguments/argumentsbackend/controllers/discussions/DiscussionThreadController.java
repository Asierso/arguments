package com.asier.arguments.argumentsbackend.controllers.discussions;

import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionDto;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface DiscussionThreadController {
    @PostMapping("/discussions")
    ResponseEntity<ServiceResponse> insert(@RequestParam String clientToken, @RequestBody DiscussionDto discussionDto, @RequestAttribute String username);
    @GetMapping("/discussions")
    ResponseEntity<ServiceResponse> findByPage(@RequestParam String clientToken, @RequestParam(defaultValue = "0") String page);
    @GetMapping("/discussions/{discussionId}")
    ResponseEntity<ServiceResponse> getDiscussion(@RequestParam String clientToken, @PathVariable String discussionId);
    @PostMapping("/discussions/{discussionId}/join")
    ResponseEntity<ServiceResponse> join(@RequestParam String clientToken, @PathVariable String discussionId, @RequestAttribute String username);
}
