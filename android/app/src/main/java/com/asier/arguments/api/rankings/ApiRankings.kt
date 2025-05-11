package com.asier.arguments.api.rankings

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.Globals
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiRankings {
    @GET("auth/rankings/{discussionId}")
    suspend fun selectByDiscussion(@Path("discussionId") discussionId: String, @Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN)  : Response<ServiceResponse>
}