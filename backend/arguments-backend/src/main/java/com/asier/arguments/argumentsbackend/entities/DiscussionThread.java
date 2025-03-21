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
import java.time.LocalDateTime;
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
    private String subject;
    @Mandatory
    private ObjectId owner;
    @Mandatory
    private LocalDateTime startTime;
    @Mandatory
    @Modifiable
    private LocalDateTime endTime;
    @Mandatory
    @Modifiable
    private int maxUsers;
    @Modifiable
    private List<String> users;

    @Override
    public String getId() {
        return BasicUtils.getIdentity(id);
    }
}
