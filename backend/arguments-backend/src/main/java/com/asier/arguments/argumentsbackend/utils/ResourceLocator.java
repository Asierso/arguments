package com.asier.arguments.argumentsbackend.utils;

import lombok.Getter;

public enum ResourceLocator {
    ARGUMENTS_PROPERTIES("/arguments.properties");
    @Getter
    private String uri;
    private ResourceLocator(String uri){
        this.uri = uri;
    }
}
