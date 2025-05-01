package com.asier.arguments.argumentsbackend.entities.user;

import com.asier.arguments.argumentsbackend.entities.Identify;
import com.asier.arguments.argumentsbackend.utils.annotations.Modifiable;
import com.asier.arguments.argumentsbackend.utils.annotations.Mandatory;
import com.asier.arguments.argumentsbackend.utils.annotations.StringFix;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

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
    @StringFix
    private String firstname;
    @Modifiable
    @StringFix
    private String lastname;
    //Username cannot be modified because is used to join entities with login token and sync
    @Mandatory
    @Indexed
    @StringFix
    private String username;
    //Calculated by Backend
    @Modifiable
    private Boolean isActive;
    //Another user data
    @Modifiable
    @StringFix
    private String description;
    @Modifiable
    private Integer level;
    @Modifiable
    private Integer xp;
    private HashMap<ObjectId, String> history;
    public String getId() {
        return id != null ? id.toHexString() : null;
    }
}
