package com.asier.arguments.api.messaging

import com.asier.arguments.api.ApiServices
import com.asier.arguments.entities.MessageCreatorDto
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.RetrofitUtils
import com.asier.arguments.utils.storage.LocalStorage

object MessagingService : ApiMessagingService {
    override suspend fun insertMessage(
        localStorage: LocalStorage,
        discussionId: String,
        message: MessageCreatorDto
    ): ServiceResponse? {
        return RetrofitUtils.getResponse(
            ApiServices.MessagingAuthService(localStorage).insertMessage(discussionId, message = message)
        )
    }

    override suspend fun getMessagesByPage(
        localStorage: LocalStorage,
        discussionId: String,
        page: Int
    ): ServiceResponse? {
        return RetrofitUtils.getResponse(
            ApiServices.MessagingAuthService(localStorage).getMessagesByPage(discussionId,page = page)
        )
    }
}