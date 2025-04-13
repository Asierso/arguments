package com.asier.arguments.utils.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenSource: () -> String) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        //Get token (token should be loaded from db using application context)
        val token = this.tokenSource()
        val req = chain.request().newBuilder()

        //Add bearer token
        if(token.isNotBlank()){
            req.addHeader("Authorization","Bearer $token")
        }

        return chain.proceed(req.build())
    }

}