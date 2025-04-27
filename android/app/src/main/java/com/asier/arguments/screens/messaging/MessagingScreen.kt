package com.asier.arguments.screens.messaging

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.theme.TopBarBackground

@SuppressLint("ContextCastToActivity")
@Composable
fun MessagingScreen(messagingScreenViewModel: MessagingScreenViewModel){
    //Activity parameters vm load
    val parameters: ActivityParameters = viewModel(LocalContext.current as ComponentActivity)
    val activityProperties: ActivityProperties = parameters.properties

    val scope = rememberCoroutineScope()
    messagingScreenViewModel.storage = activityProperties.storage

    //Change status bar color
    activityProperties.window.let {
        SideEffect {
            WindowCompat.getInsetsController(it, it.decorView)
                .isAppearanceLightStatusBars = true
            it.statusBarColor = TopBarBackground.toArgb()
        }
    }
}