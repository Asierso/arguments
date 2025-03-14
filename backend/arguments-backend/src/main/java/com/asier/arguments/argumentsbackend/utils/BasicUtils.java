package com.asier.arguments.argumentsbackend.utils;

import org.bson.types.ObjectId;

public class BasicUtils {
    public static String getIdentity(ObjectId id){
        return id != null? id.toHexString() : null;
    }
}
