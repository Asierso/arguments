package com.asier.arguments.utils.tasks

import com.asier.arguments.api.sync.SyncService
import com.asier.arguments.utils.storage.LocalStorage

class SyncTask : Task<LocalStorage> {
    override suspend fun run(param: LocalStorage?): Boolean {
        if(param == null)
            return false

        if(param.load("auth") == null)
            return true

        SyncService.sync(param)
        return true
    }
}