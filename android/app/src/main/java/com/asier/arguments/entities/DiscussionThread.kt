package com.asier.arguments.entities

import java.time.LocalDateTime



data class DiscussionThread(
    var id: String = "",
    var title: String = "",
    var author: String = "",
    var maxUsers: Int = 0,
    var users: List<String> = emptyList(),
    var createdAt: LocalDateTime? = null,
    var endAt: LocalDateTime? = null
)