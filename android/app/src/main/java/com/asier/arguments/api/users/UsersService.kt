package com.asier.arguments.api.users

import com.asier.arguments.api.ApiServices
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.UserCreatorDto
import com.asier.arguments.utils.RetrofitUtils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

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
}