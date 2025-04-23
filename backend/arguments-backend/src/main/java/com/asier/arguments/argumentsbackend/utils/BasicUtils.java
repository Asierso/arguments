package com.asier.arguments.argumentsbackend.utils;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Slf4j
public class BasicUtils {
    /**
     * Convert ObjectId into String. Returns null if provided ObjectId is null
     * @param id ObjectId to convert
     * @return Hex String
     */
    public static String getIdentity(ObjectId id){
        return id != null? id.toHexString() : null;
    }
}
