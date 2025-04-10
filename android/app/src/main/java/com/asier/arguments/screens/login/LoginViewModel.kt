package com.asier.arguments.screens.login

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.api.login.LoginService
import com.asier.arguments.entities.UserCredentials
import com.asier.arguments.misc.ActivityProperties
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.utils.storage.LocalStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private var storage by mutableStateOf<LocalStorage?>(null)
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    fun login(activityProperties: ActivityProperties? = null, scope: CoroutineScope){
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val result = LoginService.login(UserCredentials(username= username,password=password))
                Log.d("dep",username + " " + password)
                if(result != null){
                    if(StatusCodes.valueOf(result.status) == StatusCodes.SUCCESSFULLY){
                        activityProperties?.snackbarHostState?.showSnackbar(message = result.result.toString(), duration = SnackbarDuration.Long)
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