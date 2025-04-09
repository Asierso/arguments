package com.asier.arguments.api.users

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.UserCreatorDto
import com.asier.arguments.utils.Globals
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiUsers {
    @POST("users")
    suspend fun register(@Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN, @Body user: UserCreatorDto) : Response<ServiceResponse>

    @GET("users/username/{username}")
    suspend fun getByName(@Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN, @Path(value = "username") username : String) : Response<ServiceResponse>

    @GET("users/id/{userId}")
    suspend fun getById(@Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN, @Path(value = "userId") userId : String) : Response<ServiceResponse>
}