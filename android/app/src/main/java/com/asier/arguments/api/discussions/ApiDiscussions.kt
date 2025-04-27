package com.asier.arguments.api.discussions

import com.asier.arguments.entities.DiscussionCreatorDto
import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.Globals
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiDiscussions {
    @GET("auth/discussions/{discussionId}")
    suspend fun getDiscussionById(@Path("discussionId") discussionId : String, @Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN) : Response<ServiceResponse>
    @GET("auth/discussions")
    suspend fun getDiscussionsByPage(@Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN, @Query("page") page : Int = 0) : Response<ServiceResponse>
    @POST("auth/discussions")
    suspend fun createDiscussion(@Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN, @Body discussion : DiscussionCreatorDto) : Response<ServiceResponse>
}