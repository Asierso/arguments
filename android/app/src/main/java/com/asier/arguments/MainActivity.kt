package com.asier.arguments

import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.others.LoadingSpinner
import com.asier.arguments.ui.components.snackbars.BaseSnackbar
import com.asier.arguments.ui.components.snackbars.ConnectionErrorSnackbar
import com.asier.arguments.ui.components.snackbars.SnackbarInvoke
import com.asier.arguments.ui.components.snackbars.SnackbarType
import com.asier.arguments.ui.components.snackbars.SucessSnackbar
import com.asier.arguments.ui.components.snackbars.WarningSnackbar
import com.asier.arguments.ui.theme.ArgumentsTheme
import com.asier.arguments.ui.theme.Background
import com.asier.arguments.utils.storage.LocalStorage
import com.asier.arguments.utils.tasks.SyncTask
import com.asier.arguments.utils.tasks.TasksUtils

class MainActivity : ComponentActivity() {
    private lateinit var parameters: ActivityParameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val snackbarState = SnackbarHostState()
        enableEdgeToEdge()

        setContent {
            ArgumentsTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost = {
                    SnackbarHost(snackbarState) {
                        //Build snackbar invoke
                        val builtInvoke = SnackbarInvoke.from(it.visuals.message)

                        //Check if the invoke is valid (is using custom snackbar)
                        if (builtInvoke != null) {
                            when (builtInvoke.type) {
                                SnackbarType.SERVER_ERROR ->
                                    if (builtInvoke.message.isNotBlank())
                                        ConnectionErrorSnackbar(message = builtInvoke.message)
                                    else
                                        ConnectionErrorSnackbar()
                                SnackbarType.SUCCESS ->
                                    if(builtInvoke.message.isNotBlank())
                                        SucessSnackbar(message = builtInvoke.message)
                                    else
                                        SucessSnackbar()
                                SnackbarType.WARNING ->
                                    if(builtInvoke.message.isNotBlank())
                                        WarningSnackbar(message = builtInvoke.message)
                                    else
                                        WarningSnackbar()
                            }
                        } else {
                            //Show generic snackbar
                            BaseSnackbar(message = it.visuals.message)
                        }

                    }
                }) { innerPadding ->
                    Surface(color = Background) {
                        parameters = viewModel()
                        MainScreen(modifier = Modifier.padding(innerPadding), snackbarState, window)
                    }
                }
            }
        }
    }

    @Composable
    fun MainScreen(
        modifier: Modifier = Modifier,
        snackbarHostState: SnackbarHostState,
        window: Window
    ) {
        //Define screen properties
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()

        //This properties are for composable stuff and utils that works as singleton (just one per app instance)
        val activityProperties = ActivityProperties(
            navController = navController,
            snackbarHostState = snackbarHostState,
            storage = LocalStorage(LocalContext.current),
            window = window
        )

        //Set properties
        parameters.properties = activityProperties

        //TODO: REMOVE
        AppNavGraph(
            modifier = Modifier,
            start = Screen.Rankings
        )

        /*
        AppNavGraph(
            modifier = modifier,
            start = if ((activityProperties.storage.load("auth")
                    ?: "").isNotBlank()
            ) Screen.Home else Screen.Welcome
        )*/

        //Load overlay
        if (parameters.isLoading) {
            LoadingOverlay()
        }

        //Load cron tasks just once
        LaunchedEffect(Unit) {
            TasksUtils.executeAtCron(
                action = SyncTask(),
                parameters = activityProperties.storage,
                scope = scope,
                delay = 10)
        }

        //Load message screen if there's a active discussion (user tries to cheat) and makes the first nav graph screen
        LaunchedEffect(Unit) {
            if (activityProperties.storage.load("discussion") != null) {
                if(activityProperties.storage.load("discussion_expired_bypass") == null) {
                    activityProperties.navController.navigate(Screen.Messaging.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }

    @Composable
    fun LoadingOverlay() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            LoadingSpinner()
        }
    }
}