package com.asier.arguments.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.Screen
import com.asier.arguments.api.users.UsersService
import com.asier.arguments.entities.User
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.utils.GsonUtils
import com.asier.arguments.utils.storage.LocalStorage
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileEditorScreenViewModel : ViewModel() {
    //Inherited elements
    var storage by mutableStateOf<LocalStorage?>(null)
    var isAlreadyLoaded by mutableStateOf(false)

    //Consulted user data
    var userData by mutableStateOf<User?>(null)

    //Modifiable user information
    var description by mutableStateOf("")

    fun loadUserData(parameters: ActivityParameters, scope: CoroutineScope) {
        //Check if user data is already loaded
        if (isAlreadyLoaded)
            return
        isAlreadyLoaded = true

        val consultedUser = parameters.viewProfile
        parameters.isLoading = true

        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val response = UsersService.getByUsername(consultedUser.toString())
                try {
                    if (StatusCodes.valueOf(response!!.status) == StatusCodes.SUCCESSFULLY) {
                        withContext(Dispatchers.Main) {
                            //Load user data in observable fields
                            userData = GsonUtils.jsonToClass(response.result as LinkedTreeMap<*, *>)
                            description = userData!!.description
                        }

                        delay(500)
                        withContext(Dispatchers.Main) {
                            parameters.isLoading = false
                        }
                    }
                } catch (e: Exception) {
                    //Navigate to home
                    parameters.properties.navController.navigate(Screen.Home.route)
                }
            }
        }
    }

    fun updateUserData(parameters: ActivityParameters, scope: CoroutineScope){
        scope.launch {

            CoroutineScope(Dispatchers.IO).launch {
                
            }
        }
    }
}