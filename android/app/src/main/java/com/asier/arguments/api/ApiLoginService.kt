package com.asier.arguments.api

import android.util.Log
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.UserCredentials

object ApiLoginService {
    suspend fun login(userCredentials: UserCredentials) : ServiceResponse{
        var data = ApiServices.LoginService.login(userCredentials = userCredentials)
        Log.d("dep",data.raw().message)
        return data.body()!!
    }
}