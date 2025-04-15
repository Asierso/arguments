package com.asier.arguments.api.sync

import com.asier.arguments.api.ApiServices
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.RetrofitUtils
import com.asier.arguments.utils.storage.LocalStorage

object SyncService : ApiSyncService {
    override suspend fun sync(localStorage: LocalStorage): ServiceResponse? {
        return RetrofitUtils.getResponse(ApiServices.SyncAuthService(localStorage).sync())
    }
}