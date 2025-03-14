package com.asier.arguments.argumentsbackend.entities;

import com.asier.arguments.argumentsbackend.misc.Identify;
import com.asier.arguments.argumentsbackend.utils.annotations.Modifiable;
import com.asier.arguments.argumentsbackend.utils.annotations.Mandatory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Mandatory
    private String username;
    public String getId() {
        return id != null ? id.toHexString() : null;
    }
}
