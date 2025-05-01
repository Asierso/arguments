package com.asier.arguments.screens.welcome

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asier.arguments.R
import com.asier.arguments.Screen
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.ui.components.backgrounds.ArgumentsPatternBackground
import com.asier.arguments.ui.components.buttons.PrimaryButton
import com.asier.arguments.ui.components.buttons.SecondaryButton
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright0
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("ContextCastToActivity")
@Composable
fun WelcomeScreen() {
    //Activity parameters vm load
    val parameters: ActivityParameters = viewModel(LocalContext.current as ComponentActivity)
    val scope = rememberCoroutineScope()

    ArgumentsPatternBackground(alpha = .05f, modifier = Modifier.fillMaxSize().padding(5.dp))

    //If is in a loading cycle, stop loading process
    LaunchedEffect(Unit) {
        scope.launch {
            withContext(Dispatchers.IO) {
                delay(2000)
                withContext(Dispatchers.Main){
                    parameters.isLoading = false
                }
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        //Welcome title
        Column(verticalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(R.drawable.ic_hello),
                contentDescription = null,
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(R.string.welcome_text),
                fontFamily = Montserrat,
                fontSize = 50.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextBright0,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp)
            )
            Text(
                text = stringResource(R.string.welcome_subtext),
                fontFamily = Montserrat,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = TextBright0,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Column(verticalArrangement = Arrangement.Center) {
            PrimaryButton(
                text = stringResource(R.string.login_button),
                onClick = { parameters.properties.navController.navigate(Screen.Login.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp, 10.dp),
                padding = PaddingValues(5.dp, 15.dp)
            )
            SecondaryButton(
                text = stringResource(R.string.create_account_button),
                onClick = { parameters.properties.navController.navigate(Screen.Register.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp, 10.dp),
                padding = PaddingValues(5.dp, 15.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF242424)
fun WelcomeScreenPreview() {
    WelcomeScreen()
}