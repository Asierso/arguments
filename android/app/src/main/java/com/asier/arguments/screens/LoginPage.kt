package com.asier.arguments.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.asier.arguments.entities.UserLoginDto
import com.asier.arguments.ui.components.buttons.PrimaryButton
import com.asier.arguments.ui.components.inputs.IconTextInput
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright0

@Composable
fun LoginPage(navController: NavController? = null) {
    //Introduced username and password
    val username = rememberSaveable { mutableStateOf("yy") }
    val password = rememberSaveable { mutableStateOf("") }
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
        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(bottom = 10.dp)) {
            PrimaryButton(
                text = "Iniciar Sesión",
                onClick = { Login(UserLoginDto(username.value,password.value)) },
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

fun Login(userLoginDto: UserLoginDto){
    //TODO Login Ln
}

@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF242424)
fun LoginPagePreview(){
    LoginPage()
}