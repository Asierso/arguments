package com.asier.arguments.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.asier.arguments.R
import com.asier.arguments.Screen
import com.asier.arguments.ui.components.buttons.PrimaryButton
import com.asier.arguments.ui.components.buttons.SecondaryButton
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright0

@Composable
fun WelcomePage(navController: NavController? = null){
    Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        //Welcome title
        Column(verticalArrangement = Arrangement.Center) {
            Image(painter = painterResource(R.drawable.ic_hello), contentDescription = null, modifier = Modifier.size(180.dp).align(Alignment.CenterHorizontally))
            Text(
                text = "Bienvenido",
                fontFamily = Montserrat,
                fontSize = 50.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextBright0,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 20.dp)
            )
            Text(
                text = "Arguments, la red social de la discordia",
                fontFamily = Montserrat,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = TextBright0,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Column(verticalArrangement = Arrangement.Center) {
            PrimaryButton(
                text = "Iniciar Sesión",
                onClick = { navController?.navigate(Screen.Login.route) },
                modifier = Modifier.fillMaxWidth().padding(50.dp,10.dp),
                padding = PaddingValues(5.dp,15.dp))
            SecondaryButton(
                text = "Crear cuenta",
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(50.dp,10.dp),
                padding = PaddingValues(5.dp,15.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF242424)
fun WelcomePagePreview(){
    WelcomePage()
}