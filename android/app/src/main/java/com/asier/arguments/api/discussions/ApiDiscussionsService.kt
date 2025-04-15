package com.asier.arguments.api.discussions

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.storage.LocalStorage

interface ApiDiscussionsService {
    suspend fun getDiscussion(localStorage: LocalStorage) : ServiceResponse?
}