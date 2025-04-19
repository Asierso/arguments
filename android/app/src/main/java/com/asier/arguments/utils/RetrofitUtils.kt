package com.asier.arguments.utils

import com.asier.arguments.entities.ServiceResponse
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.utils.interceptors.AuthInterceptor
import com.asier.arguments.utils.storage.LocalStorage
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
            .client(getClient(false))
            .build()
    }

    fun getAuthNew(localStorage: LocalStorage) : Retrofit{
        return Retrofit.Builder()
            .baseUrl(Globals.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient(true,localStorage))
            .build()
    }
    //Uri logger
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    //Retrofit auth client (uses bearer if authenticate is true)
    private fun getClient(authenticate: Boolean, localStorage: LocalStorage? = null) : OkHttpClient{
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
        if(authenticate)
            client.addInterceptor(AuthInterceptor(tokenSource = {
                localStorage?.load("auth")!!
            }))
        return client.build()
    }

    //Make unification of body() and errorBody()
    fun getResponse(response: Response<ServiceResponse>) : ServiceResponse?{
        return if(response.isSuccessful) response.body()
            else identifyError(response.errorBody())
    }

    fun identifyError(res: ResponseBody?) : ServiceResponse{
        return try{
            if(res == null)
                throw Exception()
            val swapper = Gson().fromJson(res.string(),ServiceResponse::class.java)
            if(swapper.result != null)
                ServiceResponse(swapper.result.toString(),null)
            else
                swapper
        }catch (e: Exception){
            ServiceResponse(StatusCodes.UNKNOWN.toString(),null)
        }
    }
}