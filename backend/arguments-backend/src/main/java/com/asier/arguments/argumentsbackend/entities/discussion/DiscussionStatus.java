package com.asier.arguments.argumentsbackend.entities.discussion;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DiscussionStatus {
    STARTED(0,"started"), VOTING(1, "voting"), FINISHED(2,"finished");
    private final int id;
    private final String meaning;
}
