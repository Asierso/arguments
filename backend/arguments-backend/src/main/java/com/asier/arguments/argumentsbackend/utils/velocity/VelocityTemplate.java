package com.asier.arguments.argumentsbackend.utils.velocity;

import org.apache.velocity.VelocityContext;

public interface VelocityTemplate {
    VelocityResource build(VelocityContext context);
}
