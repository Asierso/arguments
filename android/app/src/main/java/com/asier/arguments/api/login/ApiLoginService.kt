package com.asier.arguments.api.login

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.UserCredentials

interface ApiLoginService {
    suspend fun login(userCredentials: UserCredentials) : ServiceResponse?
}