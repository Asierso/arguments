package com.asier.arguments.api.messaging

import com.asier.arguments.entities.MessageCreatorDto
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.Globals
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiMessaging {
    @POST("auth/messaging/{discussionId}")
    suspend fun insertMessage(@Path("discussionId") discussionId: String, @Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN, @Body message: MessageCreatorDto) : Response<ServiceResponse>
    @GET("auth/messaging/{discussionId}")
    suspend fun getMessagesByPage(@Path("discussionId") discussionId: String, @Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN, @Query("page") page : Int = 0) : Response<ServiceResponse>
    @GET("auth/messaging/{discussionId}/{username}")
    suspend fun getMessagesByUsername(@Path("discussionId") discussionId: String, @Path("username") username: String, @Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN) : Response<ServiceResponse>
}