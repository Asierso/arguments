package com.asier.arguments.api.sync

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.utils.Globals
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiSync {
    @POST("auth/sync")
    fun sync(@Query("clientToken") clientToken: String = Globals.API_CLIENT_TOKEN) : Response<ServiceResponse>
}