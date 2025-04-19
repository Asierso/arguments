package com.asier.arguments.api.users

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.user.UserCreatorDto
import com.asier.arguments.entities.user.UserModifiable
import com.asier.arguments.entities.user.UserModifiableDto
import com.asier.arguments.utils.storage.LocalStorage

interface ApiUsersService {
    suspend fun register(user: UserCreatorDto) : ServiceResponse?
    suspend fun getByUsername(username : String) : ServiceResponse?
    suspend fun getById(userId : String) : ServiceResponse?
    suspend fun updateByName(localStorage: LocalStorage, username: String, user: UserModifiableDto) : ServiceResponse?
}