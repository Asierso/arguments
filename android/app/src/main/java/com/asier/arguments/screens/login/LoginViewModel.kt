package com.asier.arguments.screens.login

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import com.asier.arguments.api.login.LoginService
import com.asier.arguments.entities.UserCredentials
import com.asier.arguments.misc.ActivityProperties
import com.asier.arguments.misc.StatusCodes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    fun Login(activityProperties: ActivityProperties? = null, scope: CoroutineScope, userCredentials: UserCredentials){
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val result = LoginService.login(userCredentials)
                if(result != null){
                    if(StatusCodes.valueOf(result.status) == StatusCodes.SUCCESSFULLY){
                        activityProperties?.snackbarHostState?.showSnackbar(message = result.result.toString(), duration = SnackbarDuration.Long)
                        //TODO: Home page navigation
                    }
                    if(StatusCodes.valueOf(result.status) == StatusCodes.INVALID_CREDENTIALS){
                        activityProperties?.snackbarHostState?.showSnackbar(message = "Usuario o contraseña incorrecto", duration = SnackbarDuration.Long)
                    }
                    if(StatusCodes.valueOf(result.status) == StatusCodes.UNAUTHORIZED_CLIENT){
                        activityProperties?.snackbarHostState?.showSnackbar(message = "Client token not valid", duration = SnackbarDuration.Long)
                    }
                }
            }
        }
    }
}