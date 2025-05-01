package com.asier.arguments.argumentsbackend.entities.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is returned in every server response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class ServiceResponse {
    private String status;
    private Object result;
    public <T> T getValueAs(Class<T> type) {
        if (result == null) {
            return null;
        }

        if (type.isInstance(result)) {
            return type.cast(result);
        } else {
            log.error("Cannot convert result to the specified type");
            throw new RuntimeException("Cannot convert result to the specified type");
        }
    }
}
