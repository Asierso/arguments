package com.asier.arguments.argumentsbackend.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResourceLocator {
    ARGUMENTS("/arguments.properties"),
    APPLICATION("/application.properties"),
    STATUS("/status.properties");
    private final String uri;
}
