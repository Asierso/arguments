package com.asier.arguments

/**
 * Defines all the internal routes of ever screen
 */
sealed class Screen(val route: String) {
    //Start point and user auth
    data object Welcome : Screen("welcome")
    data object Login : Screen("login")
    data object Register : Screen("register")
    //Home
    data object Home : Screen("home")
    //Profiles
    data object Profile : Screen("profile")
    data object ProfileEdit : Screen("profile.edit")
    //Discussions
    data object DiscussionCreate : Screen("discussion.create")
    //Messaging
    data object Messaging : Screen("messaging")
}