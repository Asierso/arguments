package com.asier.arguments.argumentsbackend.controllers.ranking;

import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface RankingController {
    @GetMapping("/rankings/{discussionId}")
    ResponseEntity<ServiceResponse> getRankings(@RequestParam String clientToken, @PathVariable String discussionId);
}
