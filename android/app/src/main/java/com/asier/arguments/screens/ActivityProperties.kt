package com.asier.arguments.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.asier.arguments.utils.storage.LocalStorage

data class ActivityProperties(
 var navController: NavHostController,
 var snackbarHostState: SnackbarHostState,
 var storage: LocalStorage
)