package com.asier.arguments.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtils {
    fun getNew() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(Globals.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}