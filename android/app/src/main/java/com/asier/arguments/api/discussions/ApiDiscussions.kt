package com.asier.arguments.api.discussions

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.Globals
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiDiscussions {
    @GET("auth/discussions")
    suspend fun getDiscussionsByPage(@Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN, @Query("page") page : Int = 0) : Response<ServiceResponse>
}