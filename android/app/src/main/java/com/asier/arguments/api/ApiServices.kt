package com.asier.arguments.api

import com.asier.arguments.api.users.ApiUsers
import com.asier.arguments.utils.RetrofitUtils

object ApiServices {
    val LoginService: ApiLogin = RetrofitUtils.getNew().create(ApiLogin::class.java)
    val UsersService = RetrofitUtils.getNew().create(ApiUsers::class.java)
}