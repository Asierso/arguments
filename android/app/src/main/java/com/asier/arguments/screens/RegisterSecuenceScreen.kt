package com.asier.arguments.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asier.arguments.R
import com.asier.arguments.Screen
import com.asier.arguments.api.users.UsersService
import com.asier.arguments.entities.User
import com.asier.arguments.entities.UserCreatorDto
import com.asier.arguments.entities.UserCredentials
import com.asier.arguments.misc.ActivityProperties
import com.asier.arguments.misc.PasswordPolicyCodes
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.ui.components.buttons.PrimaryButton
import com.asier.arguments.ui.components.inputs.IconTextInput
import com.asier.arguments.ui.components.others.TextCheck
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright0
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils

@Composable
fun RegisterSecuenceScreen(
    activityProperties: ActivityProperties?,
    registerViewModel: RegisterSecuenceViewModel
) {
    //Scope to make fetch
    val scope = rememberCoroutineScope()

    //Render all the screens
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
    ) {
        when (registerViewModel.step) {
            0 -> { //Ask firstname and lastname
                RegisterScreenHeader(
                    title = stringResource(R.string.register_screen_title0),
                    subtitle = stringResource(R.string.register_screen_subtitle0),
                    icon = painterResource(R.drawable.ic_confetti)
                )
                RegisterScreenBody0(registerViewModel)
                Spacer(modifier = Modifier.height(30.dp))
                RegisterScreenButtons {
                    if (registerViewModel.firstname.isBlank()) {
                        registerViewModel.uniqueTry = false
                        return@RegisterScreenButtons
                    }
                    if (registerViewModel.lastname.isBlank()) {
                        registerViewModel.uniqueTry = false
                        return@RegisterScreenButtons
                    }
                    registerViewModel.uniqueTry = true
                    registerViewModel.step = 1
                }
            }

            1 -> { //Ask username
                RegisterScreenHeader(
                    title = "${stringResource(R.string.hello_text)}, ${
                        if (registerViewModel.firstname.isBlank()) "user" else StringUtils.abbreviate(
                            registerViewModel.firstname,
                            7
                        )
                    }",
                    subtitle = stringResource(R.string.register_screen_subtitle1),
                    icon = painterResource(R.drawable.ic_welcomeuser)
                )
                RegisterScreenBody1(registerViewModel, activityProperties)
                Spacer(modifier = Modifier.height(30.dp))
                RegisterScreenButtons {
                    if (registerViewModel.uniqueUsername && checkUsernamePolicy(registerViewModel.username)) {
                        registerViewModel.uniqueTry = true
                        registerViewModel.step = 2
                    } else {
                        registerViewModel.uniqueTry = false
                    }
                }
            }

            2 -> { //Ask password
                RegisterScreenHeader(
                    title = stringResource(R.string.register_screen_title2),
                    subtitle = "Protege tu cuenta de Arguments con una contraseña. ¡Más vale prevenir que lamentar!",
                    icon = painterResource(R.drawable.ic_identification)
                )
                RegisterScreenBody2(registerViewModel)
                RegisterScreenButtons {
                    if(checkPasswords(registerViewModel) == PasswordPolicyCodes.STRONG) {
                        registerViewModel.uniqueTry = true
                        registerUser(
                            scope=scope,
                            activityProperties=activityProperties,
                            registerViewModel=registerViewModel)
                    }
                }
            }

            3 -> { //User created
                RegisterScreenHeader(
                    title = "¡Todo listo!",
                    subtitle = "Tu experiencia en Arguments acaba de comenzar",
                    icon = painterResource(R.drawable.ic_whujuu)
                )
                Spacer(modifier = Modifier.height(30.dp))
                PrimaryButton(
                    text = stringResource(R.string.start_button),
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(50.dp, 10.dp),
                    padding = PaddingValues(5.dp, 15.dp)
                )
            }
        }
    }
}

@Composable
fun RegisterScreenHeader(title: String, subtitle: String, icon: Painter) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = icon,
            contentDescription = title,
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
        )
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Montserrat,
            fontSize = 36.sp,
            color = TextBright0,
            modifier = Modifier.padding(top = 20.dp)
        )
        Text(
            text = subtitle,
            fontWeight = FontWeight.Medium,
            fontFamily = Montserrat,
            fontSize = 20.sp,
            color = TextBright0,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 21.dp, end = 21.dp, top = 10.dp)
        )
    }
}

@Composable
fun RegisterScreenBody0(registerViewModel: RegisterSecuenceViewModel) {
    Column(verticalArrangement = Arrangement.Center) {
        IconTextInput(
            modifier = Modifier.padding(bottom = 5.dp),
            onValueChanged = { registerViewModel.firstname = it },
            text = registerViewModel.firstname,
            leadingIcon = {
                Icon(painterResource(R.drawable.ic_person), contentDescription = null)
            },
            placeholder = "Nombre",
            isError = !registerViewModel.uniqueTry && registerViewModel.firstname.isBlank()
        )
        IconTextInput(
            modifier = Modifier.padding(top = 5.dp),
            onValueChanged = { registerViewModel.lastname = it },
            text = registerViewModel.lastname,
            leadingIcon = {
                Icon(painterResource(R.drawable.ic_person), contentDescription = null)
            },
            placeholder = "Apellidos",
            isError = !registerViewModel.uniqueTry && registerViewModel.lastname.isBlank()
        )
    }
}

@Composable
fun RegisterScreenBody1(
    registerViewModel: RegisterSecuenceViewModel,
    activityProperties: ActivityProperties?
) {
    val scope = rememberCoroutineScope()
    Column(verticalArrangement = Arrangement.Center) {
        IconTextInput(
            modifier = Modifier.padding(bottom = 5.dp),
            onValueChanged = {
                registerViewModel.username = it
                registerViewModel.uniqueTry = false
                if (checkUsernamePolicy(registerViewModel.username))
                    checkUserAvailable(
                        scope = scope,
                        registerViewModel = registerViewModel,
                        activityProperties = activityProperties
                    )
            },
            text = registerViewModel.username,
            leadingIcon = {
                Icon(painterResource(R.drawable.ic_person), contentDescription = null)
            },
            placeholder = "Nombre de usuario",
            isError = !registerViewModel.uniqueTry && (!registerViewModel.uniqueUsername || !checkUsernamePolicy(
                registerViewModel.username
            ))
        )
        //Username check component (only show it if username field was edited)
        if (!registerViewModel.uniqueTry) {
            TextCheck(
                isCorrect = registerViewModel.uniqueUsername && checkUsernamePolicy(
                    registerViewModel.username
                ),
                reason =
                if (registerViewModel.uniqueUsername) {
                    if (checkUsernamePolicy(registerViewModel.username))
                        "Ooh! Buen nombre"
                    else
                        "Nombre no válido"
                } else {
                    "Este usuario ya existe"
                },
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

@Composable
fun RegisterScreenBody2(registerViewModel: RegisterSecuenceViewModel) {
    Column(verticalArrangement = Arrangement.Center) {
        //Password inputs
        IconTextInput(
            modifier = Modifier.padding(bottom = 5.dp),
            onValueChanged = {
                registerViewModel.password1 = it
                registerViewModel.uniqueTry = false
            },
            text = registerViewModel.password1,
            leadingIcon = {
                Icon(painterResource(R.drawable.ic_key), contentDescription = null)
            },
            placeholder = "Contraseña",
            isError = !registerViewModel.uniqueTry && checkPasswords(registerViewModel) != PasswordPolicyCodes.STRONG)
        IconTextInput(
            modifier = Modifier.padding(bottom = 5.dp),
            onValueChanged = {
                registerViewModel.password2 = it
                registerViewModel.uniqueTry = false
            },
            text = registerViewModel.password2,
            leadingIcon = {
                Icon(painterResource(R.drawable.ic_key), contentDescription = null)
            },
            placeholder = "Repetir contraseña",
            isError = !registerViewModel.uniqueTry && checkPasswords(registerViewModel) != PasswordPolicyCodes.STRONG)
        //Password check component (only show it if username field was edited)
        if (!registerViewModel.uniqueTry) {
            TextCheck(
                isCorrect = checkPasswords(registerViewModel) == PasswordPolicyCodes.STRONG,
                reason =
                when (checkPasswords(registerViewModel)) {
                    PasswordPolicyCodes.NOT_EQUALS -> "Las contraseñas no coinciden"
                    PasswordPolicyCodes.WEAK -> "Contraseña débil"
                    PasswordPolicyCodes.TOO_SHORT -> "Contraseña demasiado corta"
                    PasswordPolicyCodes.STRONG -> "Contraseña robusta"
                    PasswordPolicyCodes.INVALID -> "Contraseña no válida"
                },
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

/**
 * Make a request to the backend to get if there's some user with the same username
 */
fun checkUserAvailable(
    scope: CoroutineScope,
    activityProperties: ActivityProperties?,
    registerViewModel: RegisterSecuenceViewModel
) {
    scope.launch {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = UsersService.getByUsername(registerViewModel.username) ?: return@launch
                registerViewModel.uniqueUsername =
                    StatusCodes.valueOf(res.status) != StatusCodes.SUCCESSFULLY
            } catch (error: Exception) {
                activityProperties?.snackbarHostState?.showSnackbar(
                    message = "Error de conexion con el servidor",
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                )
            }
        }
    }
}

/**
 * Creates user POJO and send it to server
 */
fun registerUser(
    scope: CoroutineScope,
    activityProperties: ActivityProperties?,
    registerViewModel: RegisterSecuenceViewModel
) {
    scope.launch {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userCreator = UserCreatorDto(
                    user = User(
                        username = registerViewModel.username,
                        firstname = registerViewModel.firstname,
                        lastname = registerViewModel.lastname
                    ),
                    credentials = UserCredentials(
                        username = registerViewModel.username,
                        password = registerViewModel.password1
                    )
                )
                val res = UsersService.register(userCreator) ?: throw Exception("Null response from server")

                if(StatusCodes.valueOf(res.status) == StatusCodes.SUCCESSFULLY){
                    registerViewModel.step = 3
                }

            } catch (error: Exception) {
                activityProperties?.snackbarHostState?.showSnackbar(
                    message = "Error de conexion con el servidor",
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                )
                activityProperties?.navController?.navigate(Screen.Welcome.route)
            }
        }
    }
}

/**
 * Check if passwords inside viewmodel are valid
 */
fun checkPasswords(registerViewModel: RegisterSecuenceViewModel): PasswordPolicyCodes {
    if (registerViewModel.password1.length < 6)
        return PasswordPolicyCodes.TOO_SHORT
    if (StringUtils.containsAny(registerViewModel.password1, "*/$\"'?¿¡!·`[]{}()\\|"))
        return PasswordPolicyCodes.INVALID
    if (registerViewModel.password1 != registerViewModel.password2)
        return PasswordPolicyCodes.NOT_EQUALS
    if (StringUtils.isAllLowerCase(registerViewModel.password1) ||
        StringUtils.isAllUpperCase(registerViewModel.password1) ||
        StringUtils.isAlpha(registerViewModel.password1)
    )
        return PasswordPolicyCodes.WEAK
    return PasswordPolicyCodes.STRONG
}

/**
 * Check if provided username is valid
 */
fun checkUsernamePolicy(username: String): Boolean {
    return username.isNotBlank() &&
            username.length > 2 &&
            !StringUtils.containsAny(username, "*/$\"'?¿¡!·`[]{}()\\|")
}

@Composable
fun RegisterScreenButtons(nextButton: () -> Unit) {
    Column {
        PrimaryButton(
            text = stringResource(R.string.next_button),
            onClick = { nextButton.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp, 10.dp),
            padding = PaddingValues(5.dp, 15.dp)
        )
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(R.string.alreado_registered_text)} ",
                fontFamily = Montserrat,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextBright0,
            )
            Text(
                text = stringResource(R.string.login_button),
                fontFamily = Montserrat,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                style = TextStyle(textDecoration = TextDecoration.Underline),
                color = TextBright0,
            )
        }
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF242424)
fun RegisterSecuenceScreen0Preview() {
    RegisterSecuenceScreen(null, registerViewModel = RegisterSecuenceViewModel().apply {
        step = 0
    })
}

@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF242424)
fun RegisterSecuenceScreen1Preview() {
    RegisterSecuenceScreen(null, registerViewModel = RegisterSecuenceViewModel().apply {
        step = 1
    })
}

@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF242424)
fun RegisterSecuenceScreen2Preview() {
    RegisterSecuenceScreen(null, registerViewModel = RegisterSecuenceViewModel().apply {
        step = 2
    })
}

@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF242424)
fun RegisterSecuenceScreen3Preview() {
    RegisterSecuenceScreen(null, registerViewModel = RegisterSecuenceViewModel().apply {
        step = 3
    })
}