package com.asier.arguments.entities.pages

/*
Used to handle responses that are paginated
 */
data class PageResponse<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Long,
    val size: Int,
    val number: Int
)