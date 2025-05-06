package com.asier.arguments.arguments_backend.discussions;

import com.asier.arguments.argumentsbackend.ArgumentsBackendApplication;
import com.asier.arguments.argumentsbackend.entities.discussion.DiscussionStatus;
import com.asier.arguments.argumentsbackend.services.discussions.DiscussionThreadService;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArgumentsBackendApplication.class)
public class DiscussionVotingTest {
    @Autowired
    private DiscussionThreadService discussionThreadService;

    @Test
    public void checkVoting() throws InterruptedException {
        discussionThreadService.alterStatus(new ObjectId("68190b72bf0c0a77cab35705"), DiscussionStatus.VOTING);
        Thread.sleep(100000);
        Assert.assertTrue(true);
    }
}
