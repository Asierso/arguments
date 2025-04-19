package com.asier.arguments.screens.register

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.Screen
import com.asier.arguments.api.AuthFacade
import com.asier.arguments.api.users.UsersService
import com.asier.arguments.entities.user.User
import com.asier.arguments.entities.user.UserCreatorDto
import com.asier.arguments.entities.user.UserCredentials
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.misc.PasswordPolicyCodes
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.ui.components.snackbars.SnackbarInvoke
import com.asier.arguments.ui.components.snackbars.SnackbarType
import com.asier.arguments.utils.ValidationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils

class RegisterSequenceViewModel : ViewModel() {
    //Plain user data
    var firstname by mutableStateOf("")
    var lastname by mutableStateOf("")
    var username by mutableStateOf("")
    var password1 by mutableStateOf("")
    var password2 by mutableStateOf("")

    //Register sequence flow data
    var step by mutableIntStateOf(0)
    var uniqueUsername by mutableStateOf(true)
    var uniqueVerificationError by mutableStateOf(true)
    var uniqueTry by mutableStateOf(true)

    /**
     * Check if passwords inside viewmodel are valid
     */
    fun checkPasswords(): PasswordPolicyCodes {
        return ValidationUtils.checkPasswords(password1,password2)
    }

    /**
     * Creates user POJO and send it to server
     */
    fun registerUser(
        scope: CoroutineScope,
        activityProperties: ActivityProperties?
    ) {
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userCreator = UserCreatorDto(
                        user = User(
                            username = username,
                            firstname = firstname,
                            lastname = lastname
                        ),
                        credentials = UserCredentials(
                            username = username,
                            password = password1
                        )
                    )
                    //Try to register new user
                    val res = UsersService.register(userCreator) ?: throw Exception("Null response from server")

                    if(StatusCodes.valueOf(res.status) == StatusCodes.SUCCESSFULLY){
                        step = 3
                    }

                } catch (error: Exception) {
                    //Return welcome page
                    CoroutineScope(Dispatchers.Main).launch {
                        activityProperties?.navController?.navigate(Screen.Welcome.route)
                    }

                    showServerError(activityProperties)
                }
            }
        }
    }

    fun login(
        scope: CoroutineScope,
        activityProperties: ActivityProperties?
    ){
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                AuthFacade.login(
                    activityProperties = activityProperties,
                    credentials = UserCredentials(
                        username = username,
                        password = password1
                     ),
                    onSucess = {
                        //Go to home page
                        CoroutineScope(Dispatchers.Main).launch {
                            activityProperties?.navController?.navigate(Screen.Home.route)
                        }
                    },
                    onFailure = {
                        //Show failure text and go to welcome
                        CoroutineScope(Dispatchers.Main).launch {
                            activityProperties?.navController?.navigate(Screen.Welcome.route)
                            activityProperties?.snackbarHostState?.showSnackbar(
                                message = SnackbarInvoke(
                                    SnackbarType.SERVER_ERROR
                                ).build(), duration = SnackbarDuration.Short
                            )
                        }
                    }
                )
            }
        }
    }

    /**
     * Make a request to the backend to get if there's some user with the same username
     */
    fun checkUserAvailable(
        scope: CoroutineScope,
        activityProperties: ActivityProperties?
    ) {
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val res = UsersService.getByUsername(username) ?: return@launch
                when(StatusCodes.valueOf(res.status)) {
                    StatusCodes.SUCCESSFULLY -> {
                        uniqueUsername = false
                        uniqueVerificationError = false
                    }
                    StatusCodes.NOT_FOUND -> {
                        uniqueUsername = true
                        uniqueVerificationError = false
                    }
                    else -> {
                        showServerError(activityProperties)
                        uniqueVerificationError = true
                    }
                }
            }
        }
    }

    /**
     * Check if provided username is valid
     */
    fun checkUsernamePolicy(): Boolean {
        return username.isNotBlank() &&
                username.length > 2 &&
                !StringUtils.containsAny(username, "*/$\"'?¿¡!·`[]{}()\\| ")
    }

    /**
     * If server connection fall down suddenly
     */
    suspend fun showServerError(activityProperties: ActivityProperties?){
        activityProperties?.snackbarHostState?.showSnackbar(
           message = SnackbarInvoke(SnackbarType.SERVER_ERROR).build()
        )
    }
}
