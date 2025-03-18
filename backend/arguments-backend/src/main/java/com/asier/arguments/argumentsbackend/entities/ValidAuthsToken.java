package com.asier.arguments.argumentsbackend.entities;

import com.asier.arguments.argumentsbackend.misc.Identify;
import com.asier.arguments.argumentsbackend.utils.BasicUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "auths")
public class ValidAuthsToken implements Identify {
    @Id
    private ObjectId id;
    @Indexed
    private String token;

    @Override
    public String getId(){
        return BasicUtils.getIdentity(id);
    }

    public static ValidAuthsToken toAuthToken(String token){
        return ValidAuthsToken.builder().token(token).build();
    }
}
