package com.asier.arguments.argumentsbackend.entities.rankings;

import com.asier.arguments.argumentsbackend.entities.Identify;
import com.asier.arguments.argumentsbackend.utils.annotations.Mandatory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "rankings")
public class Ranking implements Identify {
    @Id
    private ObjectId id;
    @Mandatory
    private ObjectId discussion;
    @Mandatory
    private HashMap<String,Integer> ranking;
    @Mandatory
    private HashMap<String,Integer> xpPoints;
    private String paimonVote;
    public String getId() {
        return id != null ? id.toHexString() : null;
    }
}
