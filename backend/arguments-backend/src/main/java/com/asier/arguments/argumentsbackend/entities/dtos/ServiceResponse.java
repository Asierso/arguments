package com.asier.arguments.argumentsbackend.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
