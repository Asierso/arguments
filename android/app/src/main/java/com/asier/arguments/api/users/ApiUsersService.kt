package com.asier.arguments.api.users

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.UserCreatorDto
import retrofit2.Response

interface ApiUsersService {
    suspend fun register(user: UserCreatorDto) : Response<ServiceResponse>
    suspend fun getByUsername(username : String) : Response<ServiceResponse>
    suspend fun getById(userId : String) : Response<ServiceResponse>
    suspend fun existsUsername(username: String) : Response<Boolean>
}