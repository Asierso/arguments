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
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This entity creates association between username and password to allow logins.
 * This is confident data and password are encrypted using BCrypt util
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "credentials")
public class UserCredentials implements Identify {
    @Id
    private ObjectId id;
    @Mandatory
    @Indexed
    private String username;
    @Mandatory
    @Modifiable
    private String password;
    @Override
    public String getId() {
        return BasicUtils.getIdentity(id);
    }
}