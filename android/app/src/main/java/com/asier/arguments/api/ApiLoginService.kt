package com.asier.arguments.api

import android.util.Log
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.UserCredentials
import com.asier.arguments.utils.RetrofitUtils
import com.google.gson.Gson

object ApiLoginService {
    suspend fun login(userCredentials: UserCredentials) : ServiceResponse?{
        return RetrofitUtils.getResponse(ApiServices.LoginService.login(userCredentials = userCredentials))
    }
}