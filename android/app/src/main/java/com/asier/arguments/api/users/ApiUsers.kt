package com.asier.arguments.api.users

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.entities.user.UserCreatorDto
import com.asier.arguments.entities.user.UserModifiableDto
import com.asier.arguments.utils.Globals
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiUsers {
    @POST("users")
    suspend fun register(@Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN, @Body user: UserCreatorDto) : Response<ServiceResponse>

    @GET("users/username/{username}")
    suspend fun getByName(@Path(value = "username", encoded = true) username : String, @Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN) : Response<ServiceResponse>

    @GET("users/id/{userId}")
    suspend fun getById(@Path(value = "userId", encoded = true) userId : String, @Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN) : Response<ServiceResponse>

    @PATCH("auth/users/username/{username}")
    suspend fun updateByUsername(@Path(value = "username", encoded = true) username : String, @Body user: UserModifiableDto, @Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN) : Response<ServiceResponse>

    @DELETE("auth/users/username/{username}")
    suspend fun deleteByUsername(@Path(value = "username", encoded = true) username: String, @Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN) : Response<ServiceResponse>
}