package com.asier.arguments.entities

import java.time.Instant


data class DiscussionThread(
    var id: String = "",
    var title: String = "",
    var author: String = "",
    var maxUsers: Int = 0,
    var users: List<String> = emptyList(),
    var createdAt: Instant? = null,
    var endAt: Instant? = null,
    var votes: HashMap<String,Int> = hashMapOf()
)