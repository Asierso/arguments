package com.asier.arguments.argumentsbackend.utils.annotations;

import java.lang.reflect.Field;
import java.util.*;

public class AnnotationsUtils {
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

    public static <T> void modifyEntity(T source, T changes){
        if(source == null){
            return;
        }

        for (Field field : source.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.isAnnotationPresent(Modifiable.class))
                    if(field.get(changes) != null)
                        field.set(source, field.get(changes));
            } catch (IllegalAccessException e) {
                log.error("Error at modifying entity {}", source.getClass().getSimpleName());
            }
        }
    }
}
