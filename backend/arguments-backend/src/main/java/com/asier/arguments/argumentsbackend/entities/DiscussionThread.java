package com.asier.arguments.argumentsbackend.entities;

import com.asier.arguments.argumentsbackend.misc.Identify;
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
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;

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
    private DiscussionStatus status;

    @Override
    public String getId() {
        return BasicUtils.getIdentity(id);
    }
}
