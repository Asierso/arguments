package com.asier.arguments.argumentsbackend.services.discussions;

import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionStatus;
import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionThread;
import com.asier.arguments.argumentsbackend.repositories.DiscussionThreadRepository;
import com.asier.arguments.argumentsbackend.services.discussions.processors.PaimonDiscussionQueuing;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.annotations.AnnotationsUtils;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@Service
public class DiscussionThreadServiceImpl implements DiscussionThreadService {
    @Autowired
    private DiscussionThreadRepository discussionRepository;
    @Autowired
    private PaimonDiscussionQueuing discussionQueuing;
    private final Properties props = PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS);
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

    public Page<DiscussionThread> findInPage(int page){
        return discussionRepository.findAll(PageRequest.of(page,Integer.parseInt(props.getProperty("arguments.api.discussionsPerPage")), Sort.by("endAt").descending()));
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
    public boolean alterStatus(ObjectId id, DiscussionStatus status) {
        if(id==null)
            return false;

        //Get discussion by id
        Optional<DiscussionThread> selected = discussionRepository.findById(id);
        if(selected.isEmpty())
            return false;

        //Save discussion with the status changed
        selected.get().setStatus(status);
        discussionRepository.save(selected.get());

        if(status == DiscussionStatus.VOTING){
            discussionQueuing.enqueue(selected.get());
        }

        return true;
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
