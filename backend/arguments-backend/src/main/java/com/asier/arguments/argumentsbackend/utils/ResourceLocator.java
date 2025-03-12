package com.asier.arguments.argumentsbackend.utils;

import lombok.Getter;

@Getter
public enum ResourceLocator {
    ARGUMENTS_PROPERTIES("/arguments.properties"),
    APPLICATION_PROPERTIES("/application.properties");
    private final String uri;
    private ResourceLocator(String uri){
        this.uri = uri;
    }
}
