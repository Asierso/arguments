package com.asier.arguments.argumentsbackend.services;

import org.springframework.data.domain.Page;

/**
 * This interface is for every object that will be paginated in order to execute a functional task.
 * Tasks are divided in threads, and each threads consumes one page at the same time
 * @param <T> Class
 */
public interface TaskPagination<T> {
    Page<T> findInPage(int page);
}
