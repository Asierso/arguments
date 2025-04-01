package com.asier.arguments.argumentsbackend.controllers.discussions;

import com.asier.arguments.argumentsbackend.entities.DiscussionThread;
import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface DiscussionThreadController {
    @PostMapping("/discussions")
    ResponseEntity<ServiceResponse> insert(@RequestParam String clientToken, @RequestBody DiscussionThread discussion, @RequestAttribute String username);
    @GetMapping("/discussions")
    void findByPage(@RequestParam String clientToken, @RequestParam int page);
}
