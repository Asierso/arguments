package com.asier.arguments.api.login

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.UserCredentials
import com.asier.arguments.utils.storage.LocalStorage

interface ApiLoginService {
    suspend fun login(userCredentials: UserCredentials) : ServiceResponse?
    suspend fun logout(localStorage: LocalStorage) : ServiceResponse?
}