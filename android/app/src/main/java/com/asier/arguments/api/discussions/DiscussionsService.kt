package com.asier.arguments.api.discussions

import com.asier.arguments.api.ApiServices
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.RetrofitUtils
import com.asier.arguments.utils.storage.LocalStorage

object DiscussionsService : ApiDiscussionsService{
    override suspend fun getDiscussion(localStorage: LocalStorage): ServiceResponse? {
        return RetrofitUtils.getResponse(ApiServices.DiscussionsAuthService(localStorage).getDiscussion())
    }
}