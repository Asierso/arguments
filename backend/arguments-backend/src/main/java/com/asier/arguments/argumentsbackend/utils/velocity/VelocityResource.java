package com.asier.arguments.argumentsbackend.utils.velocity;

import lombok.Builder;
import lombok.Getter;
import org.apache.velocity.VelocityContext;

@Getter
@Builder
public class VelocityResource {
    private String template;
    private VelocityContext context;

    public String getResource() {
        return "templates/" + template + ".vm";
    }
}
