package com.asier.arguments.argumentsbackend.paimon.templates;

import com.asier.arguments.argumentsbackend.entities.discussion.PaimonDiscussionThreadDto;
import com.asier.arguments.argumentsbackend.utils.velocity.VelocityResource;
import com.asier.arguments.argumentsbackend.utils.velocity.VelocityTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.velocity.VelocityContext;

@Builder
@AllArgsConstructor
public class VotingTemplate implements VelocityTemplate {
    private PaimonDiscussionThreadDto discussion;
    @Override
    public VelocityResource build(VelocityContext context) {
        context.put("usernames",discussion.getMessages().keySet().toArray());
        context.put("topic",discussion.getTopic());
        context.put("messages",discussion.getMessages());

        return VelocityResource.builder()
                .template("discussion-voting")
                .context(context)
                .build();
    }
}
