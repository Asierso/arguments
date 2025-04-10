package com.asier.arguments.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.asier.arguments.Screen
import com.asier.arguments.misc.ActivityProperties
import com.asier.arguments.ui.components.buttons.PrimaryButton
import com.asier.arguments.utils.storage.LocalStorage

@Composable
fun HomeScreen(activityProperties: ActivityProperties? = null){
    val lc = LocalStorage(LocalContext.current)
    Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Text(text = "Success", color = Color.White)
        PrimaryButton(text = "Logout", onClick = {
            lc.delete("auth")
            activityProperties?.navController?.navigate(Screen.Welcome.route)
        })
    }
}