package com.asier.arguments.screens.profile

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.Screen
import com.asier.arguments.api.users.UsersService
import com.asier.arguments.entities.user.User
import com.asier.arguments.entities.user.UserModifiable
import com.asier.arguments.entities.user.UserModifiableCredentials
import com.asier.arguments.entities.user.UserModifiableDto
import com.asier.arguments.misc.PasswordPolicyCodes
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.snackbars.SnackbarInvoke
import com.asier.arguments.ui.components.snackbars.SnackbarType
import com.asier.arguments.utils.GsonUtils
import com.asier.arguments.utils.ValidationUtils
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
    var firstname by mutableStateOf("")
    var lastname by mutableStateOf("")
    var password by mutableStateOf("")

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
                            firstname = userData!!.firstname
                            lastname = userData!!.lastname
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
                //Build user modifiable POJO with wrote data
                val userDto = UserModifiableDto(
                    user = UserModifiable(
                        firstname = if(firstname.isBlank()) null else firstname,
                        lastname = if(lastname.isBlank()) null else lastname,
                        description = null
                    ),
                    credentials = if(password.isBlank()) null else UserModifiableCredentials(
                        password = password
                    )
                )

                //Make update
                val result = UsersService.updateByName(
                    localStorage = storage!!,
                    user = userDto,
                    username = storage!!.load("user").toString())

                //Process response to show correspond snackbar
                showResponseMessage(StatusCodes.valueOf(result!!.status), parameters.properties)
            }
        }
    }

    fun updateUserDescription(parameters: ActivityParameters, scope: CoroutineScope){
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                //Create user with modified description
                val userDto = UserModifiableDto(
                    user = UserModifiable(
                        firstname = null,
                        lastname = null,
                        description = if (description.isBlank()) null else description
                    ),
                    credentials = null
                )
                val result = UsersService.updateByName(
                    localStorage = storage!!,
                    user = userDto,
                    username = storage!!.load("user").toString())

                //Process response to show correspond snackbar
                showResponseMessage(StatusCodes.valueOf(result!!.status), parameters.properties)
            }
        }
    }

    suspend fun showResponseMessage(status: StatusCodes, activityProperties: ActivityProperties){
        when(status){
            StatusCodes.SUCCESSFULLY -> {
                activityProperties.snackbarHostState.showSnackbar(
                    message = SnackbarInvoke(SnackbarType.SUCCESS).build()
                )
            }
            else -> {
                activityProperties.snackbarHostState.showSnackbar(
                    message = SnackbarInvoke(SnackbarType.SERVER_ERROR).build(),
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    fun checkPasswords(): PasswordPolicyCodes {
        return ValidationUtils.checkPasswords(password,password)
    }
}