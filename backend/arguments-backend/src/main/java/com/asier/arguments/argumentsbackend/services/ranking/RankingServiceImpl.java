package com.asier.arguments.argumentsbackend.services.ranking;

import com.asier.arguments.argumentsbackend.entities.rankings.Ranking;
import com.asier.arguments.argumentsbackend.repositories.RankingRepository;
import com.asier.arguments.argumentsbackend.utils.annotations.AnnotationsUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RankingServiceImpl implements RankingService{
    @Autowired
    private RankingRepository rankingRepository;
    @Override
    public boolean insert(Ranking ranking) {
        if(AnnotationsUtils.isNotValidEntity(ranking)){
            return false;
        }

        //Avoid multiple rankings for one discussion thread (just 1 - 1)
        if(rankingRepository.findOne(Example.of(Ranking.builder().discussionId(new ObjectId(ranking.getDiscussionId())).build())).isPresent()) {
            return false;
        }

        //Save ranking
        rankingRepository.save(ranking);
        return true;
    }

    @Override
    public Ranking select(ObjectId id) {
        if(id == null){
            return null;
        }

        //Find ranking by his id
        Optional<Ranking> selected = rankingRepository.findById(id);
        return selected.orElse(null);
    }

    @Override
    public Ranking selectByDiscussion(ObjectId discussionId) {
        if(discussionId == null){
            return null;
        }

        //Find ranking by discussion id
        Optional<Ranking> selected = rankingRepository.findOne(Example.of(Ranking.builder().discussionId(discussionId).build()));
        return selected.orElse(null);
    }

}
