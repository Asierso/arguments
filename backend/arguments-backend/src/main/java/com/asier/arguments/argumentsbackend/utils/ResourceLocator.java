package com.asier.arguments.argumentsbackend.utils;

import lombok.Getter;

@Getter
public enum ResourceLocator {
    ARGUMENTS("/arguments.properties"),
    APPLICATION("/application.properties"),
    STATUS("/status.properties");
    private final String uri;
    private ResourceLocator(String uri){
        this.uri = uri;
    }
}
