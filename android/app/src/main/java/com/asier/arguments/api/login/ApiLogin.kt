package com.asier.arguments.api.login

import com.asier.arguments.utils.Globals
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.user.UserCredentials
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiLogin{
    @POST("login")
    suspend fun login(@Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN, @Body userCredentials: UserCredentials) : Response<ServiceResponse>

    @POST("auth/logout")
    suspend fun logout(@Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN) : Response<ServiceResponse>
}