package com.asier.arguments.api.users

import com.asier.arguments.api.ApiServices
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.UserCreatorDto
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

object UsersService : ApiUsersService {
    override suspend fun register(user: UserCreatorDto): Response<ServiceResponse> {
        return ApiServices.UsersService.register(user = user)
    }

    override suspend fun getByUsername(username: String): Response<ServiceResponse> {
        return ApiServices.UsersService.getByName(username = username)
    }

    override suspend fun getById(userId: String): Response<ServiceResponse> {
        return ApiServices.UsersService.getById(userId = userId)
    }

    override suspend fun existsUsername(username: String): Response<Boolean> {
       val resp = ApiServices.UsersService.getByName(username = username)
        if(resp.isSuccessful){
            return Response.success(true)
        }
        if(resp.code() == 404){
            return Response.success(false)
        }
        return Response.error(resp.code(),"{\"error\": false}".toResponseBody("application/json".toMediaType()))
    }
}