package com.asier.arguments.api.users

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.user.UserCreatorDto
import com.asier.arguments.entities.user.UserModifiable
import com.asier.arguments.entities.user.UserModifiableDto
import com.asier.arguments.utils.Globals
import com.asier.arguments.utils.storage.LocalStorage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiUsersService {
    suspend fun register(user: UserCreatorDto) : ServiceResponse?
    suspend fun getByUsername(username : String) : ServiceResponse?
    suspend fun getById(userId : String) : ServiceResponse?
    suspend fun existsByUsername(username : String) : ServiceResponse?
    suspend fun existsById(userId : String) : ServiceResponse?
    suspend fun updateByUsername(localStorage: LocalStorage, username: String, user: UserModifiableDto) : ServiceResponse?
    suspend fun deleteByUsername(localStorage: LocalStorage, username: String) : ServiceResponse?
}