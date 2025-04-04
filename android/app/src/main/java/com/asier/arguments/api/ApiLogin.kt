package com.asier.arguments.api

import com.asier.arguments.utils.Globals
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.UserCredentials
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiLogin{
    @POST("login")
    suspend fun login(@Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN, @Body userCredentials: UserCredentials) : Response<ServiceResponse>
}