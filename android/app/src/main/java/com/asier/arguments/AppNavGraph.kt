package com.asier.arguments

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.asier.arguments.screens.LoginPage
import com.asier.arguments.screens.RegisterSecuencePage
import com.asier.arguments.screens.WelcomePage

/**
 * Manage all the navigation flow in the application
 */
@Composable
fun AppNavGraph(navController : NavHostController, modifier : Modifier) {
    NavHost(navController = navController, startDestination = Screen.Welcome.route, modifier = modifier){
        composable(Screen.Welcome.route){
            WelcomePage(navController)
        }
        composable(Screen.Login.route){
            LoginPage()
        }
        composable(Screen.Register.route){
            RegisterSecuencePage()
        }
    }
}