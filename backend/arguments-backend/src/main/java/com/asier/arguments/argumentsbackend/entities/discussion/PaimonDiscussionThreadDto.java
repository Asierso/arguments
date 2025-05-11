package com.asier.arguments.argumentsbackend.entities.discussion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaimonDiscussionThreadDto {
    private ObjectId discussionId;
    private String topic;
    private HashMap<String, List<String>> messages;
}
