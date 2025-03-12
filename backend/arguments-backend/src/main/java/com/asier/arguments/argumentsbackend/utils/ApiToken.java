package com.asier.arguments.argumentsbackend.utils;

import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;

public class ApiToken {
    public static boolean checkToken(String token){
        return token.equals(PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS_PROPERTIES).getProperty("arguments.api.token"));
    }
}
