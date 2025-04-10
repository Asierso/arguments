package com.asier.arguments

/**
 * Defines all the internal routes of ever screen
 */
sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    /*
    object Perfil : Screen("perfil")
    object Ajustes : Screen("ajustes")
    object Detalle : Screen("detalle/{id}") {
        fun createRoute(id: String) = "detalle/$id"
    }*/
}