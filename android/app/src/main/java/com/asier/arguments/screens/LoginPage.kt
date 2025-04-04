package com.asier.arguments.screens

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.asier.arguments.R
import com.asier.arguments.api.ApiLoginService
import com.asier.arguments.utils.StatusCodes
import com.asier.arguments.entities.UserCredentials
import com.asier.arguments.ui.components.buttons.PrimaryButton
import com.asier.arguments.ui.components.inputs.IconTextInput
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright0
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Objects

@Composable
fun LoginPage(navController: NavController? = null) {
    //Introduced username and password
    val username = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }

    //Coroutine scope
    val scope = rememberCoroutineScope()

    Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        //Welcome title
        Column(verticalArrangement = Arrangement.Center) {
            Icon(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier.size(180.dp).align(Alignment.CenterHorizontally),
                tint = Color.White)
            Text(
                text = "Arguments",
                fontFamily = Montserrat,
                fontSize = 50.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextBright0,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 20.dp)
            )
        }
        //Username and password fields
        Column(verticalArrangement = Arrangement.Center) {
            IconTextInput(modifier = Modifier.padding(bottom = 5.dp), onValueChanged = {username.value = it}, text = username.value, leadingIcon = {
                Icon(painterResource(R.drawable.ic_person), contentDescription = null)
            }, placeholder = "Nombre de usuario")
            IconTextInput(modifier = Modifier.padding(top = 5.dp), onValueChanged = {password.value = it}, text = password.value, leadingIcon = {
                Icon(painterResource(R.drawable.ic_key), contentDescription = null)
            }, placeholder = "Contraseña")
        }
        //Bottom buttons
        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(bottom = 10.dp)) {
            PrimaryButton(
                text = "Iniciar Sesión",
                onClick = { Login(scope,UserCredentials(username.value,password.value)) },
                modifier = Modifier.fillMaxWidth().padding(50.dp,10.dp),
                padding = PaddingValues(5.dp,15.dp)
            )
            Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "¿No tienes una cuenta? ",
                    fontFamily = Montserrat,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextBright0,
                )
                Text(
                    text = "Regístrate",
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

fun Login(scope: CoroutineScope, userCredentials: UserCredentials){
    scope.launch {
        CoroutineScope(Dispatchers.IO).launch {
            val result = ApiLoginService.login(userCredentials)
            if(Objects.isNull(result)){
                return@launch
            }
            if(StatusCodes.valueOf(result!!.status) == StatusCodes.SUCCESSFULLY){
                Log.d("deb",result.result.toString())
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF242424)
fun LoginPagePreview(){
    LoginPage()
}