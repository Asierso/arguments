package com.asier.arguments.argumentsbackend.services.ranking;


import com.asier.arguments.argumentsbackend.entities.rankings.Ranking;
import org.bson.types.ObjectId;

public interface RankingService {
    boolean insert(Ranking ranking);
    Ranking select(ObjectId id);
    Ranking selectByDiscussion(ObjectId discussionId);
}
