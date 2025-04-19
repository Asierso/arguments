package com.asier.arguments.screens.login

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.Screen
import com.asier.arguments.api.AuthFacade
import com.asier.arguments.entities.user.UserCredentials
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.utils.storage.LocalStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var storage by mutableStateOf<LocalStorage?>(null)
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    fun login(activityProperties: ActivityProperties? = null, scope: CoroutineScope){
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                //Build credentials
                val cred = UserCredentials(username= username,password=password)
                AuthFacade.login(
                    activityProperties = activityProperties,
                    credentials = cred,
                    onSucess = {
                        activityProperties?.navController?.navigate(Screen.Home.route)
                    },
                    onFailure = {
                        CoroutineScope(Dispatchers.IO).launch {
                            when (it) {
                                StatusCodes.INVALID_CREDENTIALS -> activityProperties?.snackbarHostState?.showSnackbar(
                                    message = "Usuario o contraseña incorrecto",
                                    duration = SnackbarDuration.Long
                                )
                                StatusCodes.UNAUTHORIZED_CLIENT -> activityProperties?.snackbarHostState?.showSnackbar(
                                    message = "Client token not valid",
                                    duration = SnackbarDuration.Long
                                )
                                else -> null
                            }
                        }
                    }
                )
            }
        }
    }
}