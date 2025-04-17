package com.asier.arguments.misc

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import com.asier.arguments.utils.storage.LocalStorage

data class ActivityProperties(
 var navController: NavHostController,
 var snackbarHostState: SnackbarHostState,
 var storage: LocalStorage,
 var parameters: HashMap<String, Any>
)