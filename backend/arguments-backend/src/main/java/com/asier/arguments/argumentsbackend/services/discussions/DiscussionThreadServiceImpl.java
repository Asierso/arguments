package com.asier.arguments.argumentsbackend.services.discussions;

import com.asier.arguments.argumentsbackend.entities.DiscussionThread;
import com.asier.arguments.argumentsbackend.repositories.DiscussionThreadRepository;
import com.asier.arguments.argumentsbackend.utils.annotations.AnnotationsUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DiscussionThreadServiceImpl implements DiscussionThreadService {
    @Autowired
    private DiscussionThreadRepository discussionRepository;
    @Override
    public void insert(DiscussionThread discussion) {
        discussionRepository.save(discussion);
    }

    @Override
    public DiscussionThread select(ObjectId id) {
        if(id==null)
            return null;

        Optional<DiscussionThread> selected = discussionRepository.findById(id);
        return selected.orElse(null);
    }

    @Override
    public boolean delete(ObjectId id) {
        if(id==null)
            return false;

        Optional<DiscussionThread> selected = discussionRepository.findById(id);
        selected.ifPresent(discussionRepository::delete);
        return selected.isPresent();
    }

    @Override
    public boolean update(ObjectId id, DiscussionThread changes) {
        if(id==null)
            return false;

        Optional<DiscussionThread> selected = discussionRepository.findById(id);
        if(selected.isPresent()){
            AnnotationsUtils.modifyEntity(selected.get(),changes == null? DiscussionThread.builder().build() : changes);
            discussionRepository.save(selected.get());
            return true;
        }
        return false;
    }
}
