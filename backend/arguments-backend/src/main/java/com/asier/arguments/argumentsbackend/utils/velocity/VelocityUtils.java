package com.asier.arguments.argumentsbackend.utils.velocity;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Properties;

/**
 * Allows to instantiate templates using .vm files
 * Velocity replaces ${text} using the properties saved in velocity context
 */
@Component
public class VelocityUtils {
    private final VelocityEngine engine;

    public VelocityUtils(){
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        engine = new VelocityEngine(props);
    }

    /**
     * Applies .vm template to the provided template interface
     * @param template Template building interface
     * @return String of the result of apply the template
     */
    public String applyTemplate(VelocityTemplate template){
        //Process template with velocity context data
        VelocityResource resource = template.build(new VelocityContext());
        StringWriter writer = new StringWriter();
        engine.getTemplate(resource.getResource()).merge(resource.getContext(),writer);

        //Return processed data
        return writer.toString();
    }
}
