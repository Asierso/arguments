package com.asier.arguments.argumentsbackend.entities.messaging;

import com.asier.arguments.argumentsbackend.entities.Identify;
import com.asier.arguments.argumentsbackend.utils.BasicUtils;
import com.asier.arguments.argumentsbackend.utils.annotations.Mandatory;
import com.asier.arguments.argumentsbackend.utils.annotations.Modifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Every message posted inside a DiscussionThread by Users
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "messages")
public class Message implements Identify {
    @Id
    private ObjectId id;
    @Indexed
    private ObjectId discussionId;
    @Mandatory
    @Indexed
    private String sender;
    @Mandatory
    private String message;
    private Instant sendTime;
    @Modifiable
    private String feedback;
    @Override
    public String getId(){
        return BasicUtils.getIdentity(id);
    }
    public String getDiscussionId() {return BasicUtils.getIdentity(discussionId); }
}
