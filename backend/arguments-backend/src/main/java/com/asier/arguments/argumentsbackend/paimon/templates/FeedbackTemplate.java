package com.asier.arguments.argumentsbackend.paimon.templates;

import com.asier.arguments.argumentsbackend.entities.messaging.PaimonMessageDto;
import com.asier.arguments.argumentsbackend.utils.velocity.VelocityResource;
import com.asier.arguments.argumentsbackend.utils.velocity.VelocityTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.velocity.VelocityContext;

@Builder
@AllArgsConstructor
public class FeedbackTemplate implements VelocityTemplate {
    private PaimonMessageDto message;
    @Override
    public VelocityResource build(VelocityContext context) {
        context.put("message",message.getMessage());
        context.put("topic",message.getTopic());

        return VelocityResource.builder()
                .template("message-feedback")
                .context(context)
                .build();
    }
}
