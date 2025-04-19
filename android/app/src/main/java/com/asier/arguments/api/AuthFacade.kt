package com.asier.arguments.api

import com.asier.arguments.Screen
import com.asier.arguments.api.login.LoginService
import com.asier.arguments.entities.user.UserCredentials
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.misc.StatusCodes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object AuthFacade {
    suspend fun login(activityProperties: ActivityProperties?, credentials: UserCredentials, onSucess: () -> Unit, onFailure : (status: StatusCodes) -> Unit){
        val result = LoginService.login(credentials)
        if(result != null){
            if(StatusCodes.valueOf(result.status) == StatusCodes.SUCCESSFULLY){
                activityProperties?.storage?.save("auth",result.result.toString())
                activityProperties?.storage?.save("user",credentials.username)

                //Invoke success actions on mainthread
                CoroutineScope(Dispatchers.Main).launch {
                    onSucess.invoke()
                }
            }

            //Invoke error actions on mainthread
            if(StatusCodes.valueOf(result.status) == StatusCodes.INVALID_CREDENTIALS){
                onFailure.invoke(StatusCodes.valueOf(result.status))
            }
            if(StatusCodes.valueOf(result.status) == StatusCodes.UNAUTHORIZED_CLIENT){
                onFailure.invoke(StatusCodes.valueOf(result.status))
            }
        }
    }

    suspend fun logout(activityProperties: ActivityProperties?){
        LoginService.logout(activityProperties?.storage!!)
        activityProperties.storage.delete("auth")
        activityProperties.storage.delete("user")
        withContext(Dispatchers.Main) {
            activityProperties.navController.navigate(Screen.Login.route)
        }
    }
}