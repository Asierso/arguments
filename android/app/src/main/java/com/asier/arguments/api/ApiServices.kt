package com.asier.arguments.api

import android.content.Context
import com.asier.arguments.api.login.ApiLogin
import com.asier.arguments.api.login.LoginService
import com.asier.arguments.api.users.ApiUsers
import com.asier.arguments.utils.RetrofitUtils
import com.asier.arguments.utils.storage.LocalStorage
import retrofit2.Retrofit

object ApiServices {
    fun LoginAuthService(localStorage: LocalStorage) : ApiLogin {
        return RetrofitUtils.getAuthNew(localStorage).create(ApiLogin::class.java)
    }
    val LoginService: ApiLogin = RetrofitUtils.getNew().create(ApiLogin::class.java)
    val UsersService = RetrofitUtils.getNew().create(ApiUsers::class.java)
}