package com.asier.arguments.entities

import com.asier.arguments.entities.user.User

data class DiscussionUserPairDto(
    var discussionThread: DiscussionThread,
    var user: User?
)