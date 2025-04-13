package com.asier.arguments

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.asier.arguments.misc.ActivityProperties
import com.asier.arguments.screens.HomeScreen
import com.asier.arguments.screens.login.LoginPage
import com.asier.arguments.screens.register.RegisterSecuenceScreen
import com.asier.arguments.screens.register.RegisterSecuenceViewModel
import com.asier.arguments.screens.WelcomePage
import com.asier.arguments.screens.login.LoginViewModel

/**
 * Manage all the navigation flow in the application
 */
@Composable
fun AppNavGraph(activityProperties: ActivityProperties, modifier : Modifier, start: Screen = Screen.Welcome) {
    NavHost(navController = activityProperties.navController, startDestination = start.route, modifier = modifier){
        composable(Screen.Welcome.route){
            WelcomePage(activityProperties)
        }
        composable(Screen.Login.route){
            LoginPage(activityProperties, LoginViewModel())
        }
        composable(Screen.Register.route){
            RegisterSecuenceScreen(activityProperties, RegisterSecuenceViewModel())
        }
        composable(Screen.Home.route){
            HomeScreen(activityProperties)
        }
    }
}