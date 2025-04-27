package com.asier.arguments.argumentsbackend.entities.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@Builder
public class PaimonMessageDto {
    private ObjectId discussionId;
    private String topic;
    private ObjectId messageId;
    private String message;
}
