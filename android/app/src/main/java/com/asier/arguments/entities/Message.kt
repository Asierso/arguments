package com.asier.arguments.entities

import java.time.Instant

data class Message(
    var id: String = "",
    var sender: String,
    var discussionId: String,
    var sendTime: Instant,
    var message: String
)