package com.asier.arguments.api.messaging

import com.asier.arguments.entities.MessageCreatorDto
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.storage.LocalStorage

interface ApiMessagingService {
    suspend fun insertMessage(localStorage: LocalStorage, discussionId: String, message: MessageCreatorDto) : ServiceResponse?
    suspend fun getMessagesByPage(localStorage: LocalStorage, discussionId: String, page : Int) : ServiceResponse?
}