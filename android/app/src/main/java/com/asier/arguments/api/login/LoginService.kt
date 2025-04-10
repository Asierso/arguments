package com.asier.arguments.api.login

import com.asier.arguments.api.ApiServices
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.UserCredentials
import com.asier.arguments.utils.RetrofitUtils

object LoginService : ApiLoginService {
    override suspend fun login(userCredentials: UserCredentials) : ServiceResponse?{
        return RetrofitUtils.getResponse(ApiServices.LoginService.login(userCredentials = userCredentials))
    }

    override suspend fun logout(): ServiceResponse? {
        return RetrofitUtils.getResponse(ApiServices.LoginService.logout())
    }
}