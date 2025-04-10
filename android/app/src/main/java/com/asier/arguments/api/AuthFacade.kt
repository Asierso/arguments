package com.asier.arguments.api

import androidx.compose.material3.SnackbarDuration
import com.asier.arguments.Screen
import com.asier.arguments.api.login.LoginService
import com.asier.arguments.entities.UserCredentials
import com.asier.arguments.misc.ActivityProperties
import com.asier.arguments.misc.StatusCodes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object AuthFacade {
    suspend fun login(activityProperties: ActivityProperties?, credentials: UserCredentials, onSucess: () -> Unit, onFailure : (status: StatusCodes) -> Unit){
        val result = LoginService.login(credentials)
        if(result != null){
            if(StatusCodes.valueOf(result.status) == StatusCodes.SUCCESSFULLY){
                activityProperties?.storage?.save("auth",result.result.toString())

                //Invoke success actions on mainthread
                CoroutineScope(Dispatchers.Main).launch {
                    onSucess.invoke()
                }
            }

            //Invoke error actions on mainthread
            if(StatusCodes.valueOf(result.status) == StatusCodes.INVALID_CREDENTIALS){
                CoroutineScope(Dispatchers.Main).launch {
                    onFailure.invoke(StatusCodes.valueOf(result.status))
                }
            }
            if(StatusCodes.valueOf(result.status) == StatusCodes.UNAUTHORIZED_CLIENT){
                CoroutineScope(Dispatchers.Main).launch {
                    onFailure.invoke(StatusCodes.valueOf(result.status))
                }
            }
        }
    }

    suspend fun logout(activityProperties: ActivityProperties?){
        LoginService.logout(activityProperties?.storage!!)
        activityProperties.storage.delete("auth")
        CoroutineScope(Dispatchers.Main).launch {
            activityProperties.navController.navigate(Screen.Welcome.route)
        }
    }
}