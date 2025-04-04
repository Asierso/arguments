package com.asier.arguments.api

import com.asier.arguments.utils.RetrofitUtils

object ApiServices {
    val LoginService = RetrofitUtils.getNew().create(ApiLogin::class.java)
}