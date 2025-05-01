package com.asier.arguments.argumentsbackend.utils.annotations;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class AnnotationsUtils {
    /**
     * Check if the provided entity is valid. A valid entity must have all fields annotated with @Mandatory filled
     * @param entity Entity to check
     * @return If the entity is valid
     */
    public static boolean isValidEntity(Object entity) {
        if (entity == null) {
            return false;
        }

        boolean valid = true;

        for (Iterator<?> it = Arrays.asList(entity.getClass().getDeclaredFields()).iterator(); it.hasNext() && valid; ) {
            Field field = (Field) it.next();
            field.setAccessible(true);
            try {
                if (field.isAnnotationPresent(Mandatory.class) && field.get(entity) == null) {
                    valid = false;
                }
            } catch (IllegalAccessException e) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Check if the provided entity is not valid. A not valid entity may have at least one field annotated with @Mandatory like null
     * @param entity Entity to check
     * @return If the entity is not valid
     */
    public static boolean isNotValidEntity(Object entity){
        return !isValidEntity(entity);
    }

    /**
     * Modify source entity with provided changes. This method uses @Modifiable anotation to determine if the field can be overridden or not
     *
     * @param source  Source entity
     * @param changes Same source entity with the changes to apply
     * @param <T>     Entity class
     */
    public static <T> void modifyEntity(T source, T changes) {
        if (source == null) {
            return;
        }

        for (Field field : source.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.isAnnotationPresent(Modifiable.class))
                    if (field.get(changes) != null)
                        field.set(source, field.get(changes));
            } catch (IllegalAccessException e) {
                log.error("Error at modifying entity {}", source.getClass().getSimpleName());
            }
        }
    }

    /**
     * Modify the "fix" fields of source entity with programmed fixed.
     *
     * @param source  Source entity
     * @param <T>     Entity class
     */
    public static <T> void fixEntity(T source) {
        if (source == null) {
            return;
        }

        for (Field field : source.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.isAnnotationPresent(StringFix.class))
                    if (field.get(source) != null)
                        field.set(source, ((String)field.get(source)).trim());
            } catch (IllegalAccessException e) {
                log.error("Error at fixing entity {}", source.getClass().getSimpleName());
            }
        }
    }
}
