package com.asier.arguments.argumentsbackend.controllers.ranking;

import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.services.ranking.RankingService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
@RequestMapping("/api/v1/auth")
public class RankingControllerImpl implements RankingController {
    @Autowired
    private RankingService rankingService;
    private final Properties statusProps = PropertiesUtils.getProperties(ResourceLocator.STATUS);
    @Override
    public ResponseEntity<ServiceResponse> getRankings(String clientToken, String discussionId) {
        return null;
    }
}
