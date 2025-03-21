package com.asier.arguments.argumentsbackend.entities;

import com.asier.arguments.argumentsbackend.misc.Identify;
import com.asier.arguments.argumentsbackend.utils.BasicUtils;
import com.asier.arguments.argumentsbackend.utils.annotations.Mandatory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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
    @Mandatory
    @Indexed
    private ObjectId discussionId;
    @Mandatory
    @Indexed
    private ObjectId sender;
    @Mandatory
    private String message;
    private LocalDateTime sendTime;

    @Override
    public String getId(){
        return BasicUtils.getIdentity(id);
    }
}
