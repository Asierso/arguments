package com.asier.arguments.entities

import java.time.Instant

data class Message(
    var author: String,
    var sendTime: Instant,
    var text: String
)