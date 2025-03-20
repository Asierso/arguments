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
