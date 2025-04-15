package com.asier.arguments

/**
 * Defines all the internal routes of ever screen
 */
sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Profile : Screen("profile")
}