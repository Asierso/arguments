package com.asier.arguments

sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object Login : Screen("login")
    /*
    object Perfil : Screen("perfil")
    object Ajustes : Screen("ajustes")
    object Detalle : Screen("detalle/{id}") {
        fun createRoute(id: String) = "detalle/$id"
    }*/
}