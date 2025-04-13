package com.asier.arguments.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.asier.arguments.api.AuthFacade
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.buttons.PrimaryButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(activityProperties: ActivityProperties? = null, homeScreenViewModel: HomeScreenViewModel){
    //Scope to make fetch
    val scope = rememberCoroutineScope()

        ScreenTopBar(title = "A"){

        }


    Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Text(text = "Success", color = Color.White)
        PrimaryButton(text = "Logout", onClick = {
            scope.launch {
                CoroutineScope(Dispatchers.IO).launch {
                    AuthFacade.logout(activityProperties)
                }
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title
            )
        },
        navigationIcon = {

        },
        actions = actions
    )
}