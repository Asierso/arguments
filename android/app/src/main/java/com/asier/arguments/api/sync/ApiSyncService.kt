package com.asier.arguments.api.sync

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.storage.LocalStorage

interface ApiSyncService {
    suspend fun sync(localStorage: LocalStorage) : ServiceResponse?
}