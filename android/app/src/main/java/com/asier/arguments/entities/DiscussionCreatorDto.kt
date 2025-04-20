package com.asier.arguments.entities

import java.time.Instant
import java.time.ZonedDateTime


data class DiscussionCreatorDto(
    var title: String = "",
    var maxUsers: Int = 0,
    var duration: Int = 0
)