package com.asier.arguments.argumentsbackend.entities;

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

import java.time.LocalDateTime;

/**
 * This class is used to storage connection time updates in collection like a basic "ping"
 * Server uses that collection to update "isActive" properties in users.
 * If "ping" time doesn't enter in online thresholds, "isActive" will be set to false
 */
@Data
@Document("users_sync")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSync {
    @Id
    private ObjectId id;
    @Mandatory
    @Indexed
    private String username;
    @Modifiable
    private LocalDateTime sync;
}
