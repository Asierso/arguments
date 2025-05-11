package com.asier.arguments.api.messaging

import com.asier.arguments.entities.MessageCreatorDto
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.Globals
import com.asier.arguments.utils.storage.LocalStorage
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiMessagingService {
    suspend fun insertMessage(localStorage: LocalStorage, discussionId: String, message: MessageCreatorDto) : ServiceResponse?
    suspend fun getMessagesByPage(localStorage: LocalStorage, discussionId: String, page : Int) : ServiceResponse?
    suspend fun getMessagesByUsername(localStorage: LocalStorage, discussionId: String, username: String) : ServiceResponse?
}