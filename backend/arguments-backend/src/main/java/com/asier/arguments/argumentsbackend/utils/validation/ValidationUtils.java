package com.asier.arguments.argumentsbackend.utils.validation;

import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;

import java.lang.reflect.Field;
import java.util.*;

public class ValidationUtils {
    public static boolean isValidEntity(Object entity){
        if(entity == null){
            return false;
        }

        boolean valid = true;

        for(Iterator<?> it = Arrays.asList(entity.getClass().getDeclaredFields()).iterator(); it.hasNext() && valid; ){
            Field field = (Field)it.next();
            field.setAccessible(true);
            try {
                if(field.isAnnotationPresent(Mandatory.class) && field.get(entity) == null){
                    valid = false;
                }
            } catch (IllegalAccessException e) {
                valid = false;
            }
        }
        return valid;
    }

    public static boolean checkToken(String token){
        return token.equals(PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS).getProperty("arguments.api.token"));
    }
}
