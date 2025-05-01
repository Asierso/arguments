package com.asier.arguments.argumentsbackend.entities.commons;

import com.asier.arguments.argumentsbackend.entities.Identify;
import com.asier.arguments.argumentsbackend.utils.BasicUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class is used to create a registry of the valid users auth tokens.
 * Auth tokens are stored without direct user reference.
 * When a /auth request is filtered, Backend checks if the "Bearer" token exists in that collection
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "auth_tokens")
public class ValidAuthTokens implements Identify {
    @Id
    private ObjectId id;
    @Indexed
    private String token;
    private String ip;

    @Override
    public String getId(){
        return BasicUtils.getIdentity(id);
    }

    public static ValidAuthTokens toAuthToken(String token){
        return ValidAuthTokens.builder().token(token).build();
    }
}
