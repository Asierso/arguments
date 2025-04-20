package com.asier.arguments.entities

import java.time.ZonedDateTime


data class DiscussionThread(
    var id: String = "",
    var title: String = "",
    var author: String = "",
    var maxUsers: Int = 0,
    var users: List<String> = emptyList(),
    var createdAt: ZonedDateTime? = null,
    var endAt: ZonedDateTime? = null
)