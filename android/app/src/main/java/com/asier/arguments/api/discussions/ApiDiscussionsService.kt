package com.asier.arguments.api.discussions

import com.asier.arguments.entities.DiscussionCreatorDto
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.storage.LocalStorage

interface ApiDiscussionsService {
    suspend fun getDiscussionById(localStorage: LocalStorage, discussionId: String) : ServiceResponse?
    suspend fun getDiscussionsByPage(localStorage: LocalStorage, page : Int) : ServiceResponse?
    suspend fun createDiscussion(localStorage: LocalStorage, discussion: DiscussionCreatorDto) : ServiceResponse?
}