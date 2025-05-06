package com.asier.arguments.argumentsbackend.services.discussions.components;

import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionThread;
import com.asier.arguments.argumentsbackend.entities.rankings.BenedictData;
import com.asier.arguments.argumentsbackend.entities.rankings.Ranking;
import com.asier.arguments.argumentsbackend.entities.user.User;
import com.asier.arguments.argumentsbackend.services.discussions.DiscussionMembersService;
import com.asier.arguments.argumentsbackend.services.ranking.RankingService;
import com.asier.arguments.argumentsbackend.services.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class Score {
    @Autowired
    private UserService userService;
    @Autowired
    private DiscussionMembersService membersService;
    @Autowired
    private RankingService rankingService;

    public void resolveVotations(DiscussionThread discussion){
        BenedictData benedict = getBenedict(discussion.getVotes());

        //Check if the discussion ends in a draw. If is the case, add paimon vote
        if(benedict.draw() && discussion.getPaimonVote() != null && !discussion.getPaimonVote().isBlank()){
            String username = discussion.getPaimonVote().toLowerCase().trim().replace("\"","");
            if(discussion.getVotes().containsKey(username)){
                discussion.getVotes().put(username,discussion.getVotes().get(username)+1);
                resolveVotations(discussion);
            }
        }

        //Build ranking tables
        HashSet<User> users = membersService.getUsers(new ObjectId(discussion.getId()));
        HashMap<String,Integer> xpPoints = new HashMap<>();

        //Add xp points
        for(User user : users){
            int votesPlus = discussion.getVotes().get(user.getUsername()) * 10;
            int winnerPlus = benedict.winner().getKey().equals(user.getUsername())? 50 * users.size() : 0;

            //Calculate xp and level of user
            int xp = user.getXp() == null? 0 : user.getXp() + votesPlus + winnerPlus;
            int level = user.getLevel() == null? 0 : user.getLevel();

            //Add won xp to hashmap before normalization
            xpPoints.put(user.getUsername(),xp);

            //Normalize xp
            if(xp >= 100) {
                level += xp / 100;
                xp = xp % 100;
            }

            //Save xp in user document
            user.setLevel(level);
            user.setXp(xp);
            userService.modifyXp(new ObjectId(user.getId()),level,xp);
        }

        //Create ranking entity for ranking board
        log.info("Creating ranking for discussion {}",discussion.getId());
        rankingService.insert(Ranking.builder()
                .discussion(new ObjectId(discussion.getId()))
                        .ranking(discussion.getVotes())
                        .xpPoints(xpPoints)
                        .paimonVote(benedict.draw()? discussion.getPaimonVote() : "") //Paimon vote just count if is
                .build());
    }

    private BenedictData getBenedict(HashMap<String,Integer> votes){
        boolean draw = false;
        Map.Entry<String,Integer> winner = null;

        //Check winner and if the discussion ends in a draw
        for(Map.Entry<String,Integer> voteEntry : votes.entrySet()){
            if(winner == null || winner.getValue() < voteEntry.getValue()){
                winner = voteEntry;
                draw = false;
            }

            if(voteEntry != winner && Objects.equals(winner.getValue(), voteEntry.getValue())){
                draw = true;
            }
        }

        return new BenedictData(winner,draw);
    }
}
