package com.asier.arguments.api.users

import com.asier.arguments.api.ApiServices
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.user.UserCreatorDto
import com.asier.arguments.entities.user.UserModifiableDto
import com.asier.arguments.utils.RetrofitUtils
import com.asier.arguments.utils.storage.LocalStorage

object UsersService : ApiUsersService {
    override suspend fun register(user: UserCreatorDto): ServiceResponse? {
        return RetrofitUtils.getResponse(ApiServices.UsersService.register(user = user))
    }

    override suspend fun getByUsername(username: String): ServiceResponse? {
        return RetrofitUtils.getResponse(ApiServices.UsersService.getByName(username = username))
    }

    override suspend fun getById(userId: String): ServiceResponse? {
        return RetrofitUtils.getResponse(ApiServices.UsersService.getById(userId = userId))
    }

    override suspend fun updateByUsername(localStorage: LocalStorage, username: String, user: UserModifiableDto): ServiceResponse? {
        return RetrofitUtils.getResponse(ApiServices.UsersAuthService(localStorage).updateByUsername(username = username, user = user))
    }

    override suspend fun deleteByUsername(localStorage: LocalStorage, username: String) : ServiceResponse?{
        return RetrofitUtils.getResponse(ApiServices.UsersAuthService(localStorage).deleteByUsername(username = username))
    }
}