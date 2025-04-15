package com.asier.arguments.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.others.UserAlt
import com.asier.arguments.ui.components.topbars.ProfileTopBar
import com.asier.arguments.ui.theme.Background
import com.asier.arguments.ui.theme.TopBarBackground

@Composable
fun ProfileScreen(activityProperties: ActivityProperties? = null, profileScreenViewModel: ProfileScreenViewModel){
    //Scope to make fetch
    val scope = rememberCoroutineScope()
    profileScreenViewModel.storage = activityProperties?.storage
    profileScreenViewModel.loadUsername()

    //Change status bar color
    activityProperties?.window?.let {
        SideEffect {
            WindowCompat.getInsetsController(it, it.decorView)
                .isAppearanceLightStatusBars = true
            it.statusBarColor = TopBarBackground.toArgb()
        }
    }

    ProfileTopBar(title = profileScreenViewModel.username,
        modifier = Modifier.fillMaxWidth(),
        profile = {
            UserAlt(name = profileScreenViewModel.username) {
        }
        })

    Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        //Welcome title
    }
}