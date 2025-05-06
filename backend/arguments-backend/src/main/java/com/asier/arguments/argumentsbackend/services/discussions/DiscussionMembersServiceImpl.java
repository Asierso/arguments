package com.asier.arguments.argumentsbackend.services.discussions;

import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionStatus;
import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionThread;
import com.asier.arguments.argumentsbackend.entities.user.User;
import com.asier.arguments.argumentsbackend.repositories.DiscussionThreadRepository;
import com.asier.arguments.argumentsbackend.services.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Service
public class DiscussionMembersServiceImpl implements DiscussionMembersService {
    @Autowired
    private DiscussionThreadRepository discussionRepository;
    @Autowired
    private UserService userService;
    @Override
    public int join(ObjectId id, String username){
        if(id==null)
            return 1;

        Optional<DiscussionThread> selected = discussionRepository.findById(id);

        //Check if discussion thread exists
        if(selected.isEmpty())
            return 1;

        DiscussionThread discussion = selected.get();

        //Check if discussion thread is not expired
        if(discussion.getEndAt().isBefore(Instant.now())){
            return 2;
        }

        //Check if discussion thread is full
        if(discussion.getMaxUsers() <= discussion.getUsers().size()){
            return 3;
        }

        //Check if user is already joined
        if(discussion.getUsers().contains(username)){
            return 4;
        }

        //Add username to discussion users list
        discussion.getUsers().add(username);
        discussion.getVotes().put(username,0);
        discussionRepository.save(discussion);
        return 0;
    }


    @Override
    public int voteIn(ObjectId id, String target, String actor) {
        if(id == null)
            return 1;

        Optional<DiscussionThread> selected = discussionRepository.findById(id);
        if(selected.isPresent()){
            DiscussionThread discussion = selected.get();
            //User isn't in scoreboard
            if(target == null || target.isBlank() || !discussion.getVotes().containsKey(target) || actor == null || actor.isBlank()){
                return 2;
            }

            //Vote time doesn't proceed now
            if(discussion.getEndAt().isAfter(Instant.now()) || discussion.getVotingGraceAt().isBefore(Instant.now())){
                return 3;
            }

            //User tries to vote more than one time (maybe cheating)
            if(discussion.getVoteCache().contains(target)){
                return 4;
            }

            //Update votes and save user in cache (avoid more than one vote per user)
            discussion.getVotes().put(target,discussion.getVotes().get(target)+1);
            discussion.getVoteCache().add(actor);
            discussionRepository.save(discussion);
            return 0;
        }
        return 1;
    }

    @Override
    public int votePaimonIn(ObjectId id, String target) {
        if(id == null)
            return 1;

        Optional<DiscussionThread> selected = discussionRepository.findById(id);
        if(selected.isPresent()){
            DiscussionThread discussion = selected.get();
            //User isn't in scoreboard
            if(target == null || target.isBlank() || !discussion.getVotes().containsKey(target)){
                return 2;
            }

            //Vote time doesn't proceed now
            if(discussion.getEndAt().isAfter(Instant.now()) || discussion.getVotingGraceAt().isBefore(Instant.now())){
                return 3;
            }

            //User tries to vote more than one time (maybe cheating)
            if(discussion.getVoteCache().contains(target)){
                return 4;
            }

            //Update votes and save user in cache (avoid more than one vote per user)
            discussion.setPaimonVote(target);
            discussionRepository.save(discussion);
            return 0;
        }
        return 1;
    }

    @Override
    public HashSet<User> getUsers(ObjectId id) {
        if(id == null)
            return null;

        HashSet<User> users = new HashSet<>();
        Optional<DiscussionThread> selected = discussionRepository.findById(id);

        if(selected.isPresent() && selected.get().getUsers() != null){
            //Add all users to the set
            for(String username : selected.get().getUsers()){
                User found = userService.select(username);
                if(found != null) {
                    users.add(found);
                }
            }
        }

        return users;
    }
}
