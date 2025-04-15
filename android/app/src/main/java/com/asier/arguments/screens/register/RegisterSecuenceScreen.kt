package com.asier.arguments.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.asier.arguments.R
import com.asier.arguments.Screen
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.misc.PasswordPolicyCodes
import com.asier.arguments.ui.components.buttons.PrimaryButton
import com.asier.arguments.ui.components.inputs.IconTextInput
import com.asier.arguments.ui.components.others.TextCheck
import com.asier.arguments.ui.theme.Background
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright0
import org.apache.commons.lang3.StringUtils

@Composable
fun RegisterSecuenceScreen(
    activityProperties: ActivityProperties?,
    registerViewModel: RegisterSecuenceViewModel
) {
    //Scope to make fetch
    val scope = rememberCoroutineScope()

    //Change status bar color
    activityProperties?.window?.let {
        SideEffect {
            WindowCompat.getInsetsController(it, it.decorView)
                .isAppearanceLightStatusBars = true
            it.statusBarColor = Background.toArgb()
        }
    }

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

                //Bottom buttons
                RegisterScreenButtons(
                    nextButtonActions = {
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
                    },
                    loginTextActions = {
                        activityProperties?.navController?.navigate(Screen.Login.route)
                    })
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

                //Bottom buttons
                RegisterScreenButtons(
                    nextButtonActions = {
                        if (registerViewModel.uniqueUsername && registerViewModel.checkUsernamePolicy() && !registerViewModel.uniqueVerificationError) {
                            registerViewModel.uniqueTry = true
                            registerViewModel.step = 2
                        } else {
                            registerViewModel.uniqueTry = false
                        }
                    },
                    loginTextActions = {
                        activityProperties?.navController?.navigate(Screen.Login.route)
                    })
            }

            2 -> { //Ask password
                RegisterScreenHeader(
                    title = stringResource(R.string.register_screen_title2),
                    subtitle = stringResource(R.string.register_screen_subtitle2),
                    icon = painterResource(R.drawable.ic_identification)
                )
                RegisterScreenBody2(registerViewModel)

                //Bottom buttons
                RegisterScreenButtons(
                    nextButtonActions = {
                        if (registerViewModel.checkPasswords() == PasswordPolicyCodes.STRONG) {
                            registerViewModel.uniqueTry = true
                            //Try to register
                            registerViewModel.registerUser(
                                scope = scope,
                                activityProperties = activityProperties
                            )
                        }
                    },
                    loginTextActions = {
                        activityProperties?.navController?.navigate(Screen.Login.route)
                    }
                )
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
                    onClick = {
                        //Login with created user credentials
                        registerViewModel.login(
                            scope = scope,
                            activityProperties = activityProperties
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(50.dp, 10.dp),
                    padding = PaddingValues(5.dp, 15.dp)
                )
            }
        }
    }
}

/**
 * Header prints screen icon, title and subtitle
 */
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

/**
 * This body corresponds to firstname and lastname input
 */
@Composable
fun RegisterScreenBody0(registerViewModel: RegisterSecuenceViewModel) {
    Column(verticalArrangement = Arrangement.Center) {
        //Firstname input
        IconTextInput(
            modifier = Modifier.padding(bottom = 5.dp),
            onValueChanged = { registerViewModel.firstname = it },
            text = registerViewModel.firstname,
            leadingIcon = {
                Icon(painterResource(R.drawable.ic_person), contentDescription = null)
            },
            placeholder = stringResource(R.string.register_firstname_field),
            isError = !registerViewModel.uniqueTry && registerViewModel.firstname.isBlank()
        )
        //Lastname input
        IconTextInput(
            modifier = Modifier.padding(top = 5.dp),
            onValueChanged = { registerViewModel.lastname = it },
            text = registerViewModel.lastname,
            leadingIcon = {
                Icon(painterResource(R.drawable.ic_person), contentDescription = null)
            },
            placeholder = stringResource(R.string.register_lastname_field),
            isError = !registerViewModel.uniqueTry && registerViewModel.lastname.isBlank()
        )
    }
}

/**
 * This body corresponds to username input
 */
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
                if (registerViewModel.checkUsernamePolicy())
                    registerViewModel.checkUserAvailable(
                        scope = scope,
                        activityProperties = activityProperties
                    )
            },
            text = registerViewModel.username,
            leadingIcon = {
                Icon(painterResource(R.drawable.ic_person), contentDescription = null)
            },
            placeholder = stringResource(R.string.username_field),
            /*
            Should be painted life error if:
            - Is empty but contained data before
            - User not valid
            - The user exists
            - Server error
             */
            isError = (registerViewModel.uniqueVerificationError && !registerViewModel.uniqueTry) ||
                    (!registerViewModel.uniqueTry && (!registerViewModel.uniqueUsername || !registerViewModel.checkUsernamePolicy()))
        )
        //Username check component (only show it if username field was edited)
        if (!registerViewModel.uniqueTry) {
            TextCheck(
                isCorrect = !registerViewModel.uniqueVerificationError && registerViewModel.uniqueUsername && registerViewModel.checkUsernamePolicy(),
                reason =
                if(registerViewModel.uniqueVerificationError){
                    stringResource(R.string.register_userpolicy_error)
                }
                else if (registerViewModel.uniqueUsername) {
                    if (registerViewModel.checkUsernamePolicy())
                        stringResource(R.string.register_userpolicy_ok)
                    else
                        stringResource(R.string.register_userpolicy_invalid)
                } else {
                    stringResource(R.string.register_userpolicy_repeated)
                },
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

/**
 * This body corresponds to password input
 */
@Composable
fun RegisterScreenBody2(registerViewModel: RegisterSecuenceViewModel) {
    Column(verticalArrangement = Arrangement.Center) {
        //Password input (pw1)
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
            placeholder = stringResource(R.string.password_field),
            isPassword = true,
            isError = !registerViewModel.uniqueTry && registerViewModel.checkPasswords() != PasswordPolicyCodes.STRONG
        )
        //Password input (pw2)
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
            placeholder = stringResource(R.string.register_password2_field),
            isPassword = true,
            isError = !registerViewModel.uniqueTry && registerViewModel.checkPasswords() != PasswordPolicyCodes.STRONG
        )
        //Password check component (only show it if username field was edited)
        if (!registerViewModel.uniqueTry) {
            TextCheck(
                isCorrect = registerViewModel.checkPasswords() == PasswordPolicyCodes.STRONG,
                reason =
                when (registerViewModel.checkPasswords()) {
                    PasswordPolicyCodes.NOT_EQUALS -> stringResource(R.string.register_passwordpolicy_notequals)
                    PasswordPolicyCodes.WEAK -> stringResource(R.string.register_passwordpolicy_weak)
                    PasswordPolicyCodes.TOO_SHORT -> stringResource(R.string.register_passwordpolicy_tooshort)
                    PasswordPolicyCodes.STRONG -> stringResource(R.string.register_passwordpolicy_strong)
                    PasswordPolicyCodes.INVALID -> stringResource(R.string.register_passwordpolicy_invalid)
                },
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

/**
 * Bottom buttons to go further in the registration process or go login
 */
@Composable
fun RegisterScreenButtons(nextButtonActions: () -> Unit, loginTextActions: () -> Unit) {
    Column {
        PrimaryButton(
            text = stringResource(R.string.next_button),
            onClick = { nextButtonActions.invoke() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp, 10.dp),
            padding = PaddingValues(5.dp, 15.dp)
        )
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp)
                .clickable { loginTextActions.invoke() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(R.string.already_registered_text)} ",
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