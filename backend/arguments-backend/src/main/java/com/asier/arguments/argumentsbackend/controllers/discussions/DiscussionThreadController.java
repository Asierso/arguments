package com.asier.arguments.argumentsbackend.controllers.discussions;

import org.springframework.web.bind.annotation.GetMapping;

public interface DiscussionThreadController {
    @GetMapping("/discussions")
    void findByRange();
}
