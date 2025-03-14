package com.asier.arguments.argumentsbackend.utils;

import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;

@Slf4j
public class BasicUtils {
    public static String getIdentity(ObjectId id){
        return id != null? id.toHexString() : null;
    }
    public static boolean checkToken(String token){
        return token.equals(PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS).getProperty("arguments.api.token"));
    }
}
