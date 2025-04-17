package com.asier.arguments.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.api.users.UsersService
import com.asier.arguments.entities.User
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.utils.GsonUtils
import com.asier.arguments.utils.storage.LocalStorage
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileScreenViewModel : ViewModel() {
    //Inherited elements
    var storage by mutableStateOf<LocalStorage?>(null)
    var username by mutableStateOf("")

    //Consulted user data
    var userData by mutableStateOf<User?>(null)

    //Loads username from DB
    fun loadUsername(){
        if(username.isNotBlank())
            return

        storage?.load("user")?.let {
            username = it
        }
    }

    fun loadUserData(activityProperties: ActivityProperties, scope: CoroutineScope){
        val consultedUser = activityProperties.parameters["viewProfile"]?: ""
        if(username.isBlank())
            return

        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val response = UsersService.getByUsername(consultedUser.toString())
                if(StatusCodes.valueOf(response!!.status) == StatusCodes.SUCCESSFULLY){
                    withContext(Dispatchers.Main){
                        userData = GsonUtils.jsonToClass(response.result as LinkedTreeMap<*, *>)
                    }
                }
            }
        }
    }
}