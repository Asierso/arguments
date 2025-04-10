package com.asier.arguments.misc

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController

data class ActivityProperties(
 var navController: NavHostController,
 var snackbarHostState: SnackbarHostState
)