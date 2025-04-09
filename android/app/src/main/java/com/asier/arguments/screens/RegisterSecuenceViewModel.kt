package com.asier.arguments.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.Screen
import com.asier.arguments.api.users.UsersService
import com.asier.arguments.entities.User
import com.asier.arguments.entities.UserCreatorDto
import com.asier.arguments.entities.UserCredentials
import com.asier.arguments.misc.ActivityProperties
import com.asier.arguments.misc.PasswordPolicyCodes
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.ui.components.snackbars.SnackbarInvoke
import com.asier.arguments.ui.components.snackbars.SnackbarType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils

class RegisterSecuenceViewModel : ViewModel() {
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
        if (password1.length < 6)
            return PasswordPolicyCodes.TOO_SHORT
        if (StringUtils.containsAny(password1, "*/$\"'?¿¡!·`[]{}()\\| "))
            return PasswordPolicyCodes.INVALID
        if (password1 != password2)
            return PasswordPolicyCodes.NOT_EQUALS
        if (StringUtils.isAllLowerCase(password1) ||
            StringUtils.isAllUpperCase(password1) ||
            StringUtils.isAlpha(password1)
        )
            return PasswordPolicyCodes.WEAK
        return PasswordPolicyCodes.STRONG
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

    /**
     * Make a request to the backend to get if there's some user with the same username
     */
    fun checkUserAvailable(
        scope: CoroutineScope,
        activityProperties: ActivityProperties?
    ) {
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val res = UsersService.getByUsername(username) ?: return@launch
                    uniqueUsername = StatusCodes.valueOf(res.status) != StatusCodes.SUCCESSFULLY
                    uniqueVerificationError = false
                } catch (error: Exception) {
                    showServerError(activityProperties)
                    uniqueVerificationError = true
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
