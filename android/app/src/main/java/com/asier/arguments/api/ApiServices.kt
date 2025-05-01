package com.asier.arguments.api

import android.content.Context
import com.asier.arguments.api.discussions.ApiDiscussions
import com.asier.arguments.api.discussions.DiscussionsService
import com.asier.arguments.api.login.ApiLogin
import com.asier.arguments.api.login.LoginService
import com.asier.arguments.api.messaging.ApiMessaging
import com.asier.arguments.api.sync.ApiSync
import com.asier.arguments.api.sync.SyncService
import com.asier.arguments.api.users.ApiUsers
import com.asier.arguments.utils.RetrofitUtils
import com.asier.arguments.utils.storage.LocalStorage
import retrofit2.Retrofit

object ApiServices {
    fun LoginAuthService(localStorage: LocalStorage) : ApiLogin {
        return RetrofitUtils.getAuthNew(localStorage).create(ApiLogin::class.java)
    }
    fun SyncAuthService(localStorage: LocalStorage) : ApiSync {
        return RetrofitUtils.getAuthNew(localStorage).create(ApiSync::class.java)
    }
    fun UsersAuthService(localStorage: LocalStorage) : ApiUsers {
        return RetrofitUtils.getAuthNew(localStorage).create(ApiUsers::class.java)
    }
    val LoginService: ApiLogin = RetrofitUtils.getNew().create(ApiLogin::class.java)
    val UsersService = RetrofitUtils.getNew().create(ApiUsers::class.java)
    fun DiscussionsAuthService(localStorage: LocalStorage) : ApiDiscussions {
        return RetrofitUtils.getAuthNew(localStorage).create(ApiDiscussions::class.java)
    }
    fun MessagingAuthService(localStorage: LocalStorage) : ApiMessaging {
        return RetrofitUtils.getAuthNew(localStorage).create(ApiMessaging::class.java)
    }
}