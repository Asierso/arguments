package com.asier.arguments.argumentsbackend.entities.discussion;

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
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;

/**
 * This class represents every discussion thread created by Users
 */
@Data
@Document(collection = "discussions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscussionThread implements Identify {
    @Id
    private ObjectId id;
    @Mandatory
    private String title;
    private String author;
    private Instant createdAt;
    @Modifiable
    private Instant endAt;
    @Mandatory
    @Modifiable
    private Integer maxUsers;
    @Modifiable
    private HashSet<String> users;

    @Override
    public String getId() {
        return BasicUtils.getIdentity(id);
    }
}
