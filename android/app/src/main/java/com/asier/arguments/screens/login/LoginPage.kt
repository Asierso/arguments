package com.asier.arguments.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.asier.arguments.R
import com.asier.arguments.Screen
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.buttons.PrimaryButton
import com.asier.arguments.ui.components.inputs.IconTextInput
import com.asier.arguments.ui.theme.Background
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright0

@Composable
fun LoginPage(activityProperties: ActivityProperties? = null, loginViewModel: LoginViewModel) {
    //Coroutine scope
    val scope = rememberCoroutineScope()

    //Change status bar color
    activityProperties?.window?.let {
        SideEffect {
            WindowCompat.getInsetsController(it, it.decorView)
                .isAppearanceLightStatusBars = true
            it.statusBarColor = Background.toArgb()
        }
    }

    Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        //Welcome title
        Column(verticalArrangement = Arrangement.Center) {
            Icon(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.CenterHorizontally),
                tint = Color.White)
            Text(
                text = "Arguments",
                fontFamily = Montserrat,
                fontSize = 50.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextBright0,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp)
            )
        }
        //Username and password fields
        Column(verticalArrangement = Arrangement.Center) {
            IconTextInput(modifier = Modifier.padding(bottom = 5.dp), onValueChanged = {loginViewModel.username = it}, text = loginViewModel.username, leadingIcon = {
                Icon(painterResource(R.drawable.ic_person), contentDescription = null)
            }, placeholder = stringResource(R.string.username_field))
            IconTextInput(modifier = Modifier.padding(top = 5.dp), onValueChanged = {loginViewModel.password = it}, text = loginViewModel.password, leadingIcon = {
                Icon(painterResource(R.drawable.ic_key), contentDescription = null)
            }, placeholder = stringResource(R.string.password_field), isPassword = true)
        }
        //Bottom buttons
        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(bottom = 10.dp)) {
            PrimaryButton(
                text = stringResource(R.string.login_button),
                onClick = { loginViewModel.login(activityProperties,scope) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp, 10.dp),
                padding = PaddingValues(5.dp,15.dp)
            )
            Row(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp)
                .clickable { activityProperties?.navController?.navigate(Screen.Register.route) },
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${stringResource(R.string.without_registered_text)} ",
                    fontFamily = Montserrat,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextBright0,
                )
                Text(
                    text = stringResource(R.string.without_account_register_text),
                    fontFamily = Montserrat,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    color = TextBright0,
                )
            }

        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF242424)
fun LoginPagePreview(){
    LoginPage(loginViewModel = LoginViewModel())
}