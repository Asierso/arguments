package com.asier.arguments.api.users

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.UserCreatorDto
import retrofit2.Response

interface ApiUsersService {
    suspend fun register(user: UserCreatorDto) : ServiceResponse?
    suspend fun getByUsername(username : String) : ServiceResponse?
    suspend fun getById(userId : String) : ServiceResponse?
    suspend fun updateByName(username: String, user: UserCreatorDto) : ServiceResponse?
}