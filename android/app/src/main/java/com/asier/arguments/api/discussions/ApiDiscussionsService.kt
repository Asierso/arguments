package com.asier.arguments.api.discussions

import com.asier.arguments.entities.DiscussionCreatorDto
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.Globals
import com.asier.arguments.utils.storage.LocalStorage
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiDiscussionsService {
    suspend fun getDiscussionById(localStorage: LocalStorage, discussionId: String) : ServiceResponse?
    suspend fun getDiscussionsByPage(localStorage: LocalStorage, page : Int) : ServiceResponse?
    suspend fun createDiscussion(localStorage: LocalStorage, discussion: DiscussionCreatorDto) : ServiceResponse?
    suspend fun joinDiscussionById(localStorage: LocalStorage, discussionId: String) : ServiceResponse?
    suspend fun voteTo(localStorage: LocalStorage, discussionId : String, userVote : String) : ServiceResponse?
}