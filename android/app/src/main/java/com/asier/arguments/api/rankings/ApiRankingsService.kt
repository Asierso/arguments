package com.asier.arguments.api.rankings

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.storage.LocalStorage

interface ApiRankingsService {
    suspend fun selectByDiscussion(localStorage: LocalStorage, discussionId: String) : ServiceResponse?
}