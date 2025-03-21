package com.asier.arguments.argumentsbackend.entities;

import com.asier.arguments.argumentsbackend.misc.Identify;
import com.asier.arguments.argumentsbackend.utils.annotations.Modifiable;
import com.asier.arguments.argumentsbackend.utils.annotations.Mandatory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * This is the public User. People can see that information in Arguments social network
 * User entity represents all de non-critic data about the user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class User implements Identify {
    @Id
    private ObjectId id;
    @Modifiable
    private String firstname;
    @Modifiable
    private String lastname;
    //Username cannot be modified because is used to join entities with login token and sync
    @Mandatory
    @Indexed
    private String username;
    //Calculated by Backend
    @Modifiable
    private Boolean isActive;
    public String getId() {
        return id != null ? id.toHexString() : null;
    }
}
