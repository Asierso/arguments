package com.asier.arguments.argumentsbackend.entities;

import com.asier.arguments.argumentsbackend.utils.validation.Mandatory;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.asier.arguments.argumentsbackend.entities.security.HashedPassword;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    @Id
    private ObjectId id;
    @Mandatory
    private String firstname;
    @Mandatory
    private String lastname;
    @Mandatory
    private String username;
    private HashedPassword password;

    public String getId() {
        return id != null ? id.toHexString() : null;
    }
}
