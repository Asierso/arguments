package com.asier.arguments.api.rankings

import com.asier.arguments.api.ApiServices
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.RetrofitUtils
import com.asier.arguments.utils.storage.LocalStorage

object RankingsService : ApiRankingsService {
    override suspend fun selectByDiscussion(
        localStorage: LocalStorage,
        discussionId: String
    ): ServiceResponse? {
        return RetrofitUtils.getResponse(
            ApiServices.RankingsAuthService(localStorage).selectByDiscussion(discussionId=discussionId)
        )
    }
}