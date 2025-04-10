package com.asier.arguments.utils

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.misc.StatusCodes
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtils {
    fun getNew() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(Globals.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    //Uri logger
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    //Retrofit client
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    //Make unification of body() and errorBody()
    fun getResponse(response: Response<ServiceResponse>) : ServiceResponse?{
        return if(response.isSuccessful) response.body()
            else identifyError(response.errorBody())
    }

    fun identifyError(res: ResponseBody?) : ServiceResponse{
        return try{
            if(res == null)
                throw Exception()
            Gson().fromJson(res.string(),ServiceResponse::class.java)
        }catch (e: Exception){
            ServiceResponse(StatusCodes.UNKNOWN.toString(),null)
        }
    }
}