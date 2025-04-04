package com.asier.arguments

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.asier.arguments.misc.ActivityProperties
import com.asier.arguments.screens.LoginPage
import com.asier.arguments.screens.RegisterSecuencePage
import com.asier.arguments.screens.WelcomePage

/**
 * Manage all the navigation flow in the application
 */
@Composable
fun AppNavGraph(activityProperties: ActivityProperties, modifier : Modifier) {
    NavHost(navController = activityProperties.navController, startDestination = Screen.Welcome.route, modifier = modifier){
        composable(Screen.Welcome.route){
            WelcomePage(activityProperties)
        }
        composable(Screen.Login.route){
            LoginPage(activityProperties)
        }
        composable(Screen.Register.route){
            RegisterSecuencePage()
        }
    }
}