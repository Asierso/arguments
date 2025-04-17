package com.asier.arguments

import android.os.Bundle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.navigation.compose.rememberNavController
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.others.LoadingSpinner
import com.asier.arguments.ui.components.snackbars.BaseSnackbar
import com.asier.arguments.ui.components.snackbars.ConnectionErrorSnackbar
import com.asier.arguments.ui.components.snackbars.SnackbarInvoke
import com.asier.arguments.ui.components.snackbars.SnackbarType
import com.asier.arguments.ui.theme.ArgumentsTheme
import com.asier.arguments.ui.theme.Background
import com.asier.arguments.utils.storage.LocalStorage
import java.util.HashMap

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val snackbarState = SnackbarHostState()
        enableEdgeToEdge()

        setContent {
            ArgumentsTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),  snackbarHost = { SnackbarHost(snackbarState){
                    //Build snackbar invoke
                    val builtInvoke = SnackbarInvoke.from(it.visuals.message)

                    //Check if the invoke is valid (is using custom snackbar)
                    if(builtInvoke != null){
                        when(builtInvoke.type){
                            SnackbarType.SERVER_ERROR ->
                                if(builtInvoke.message.isNotBlank())
                                    ConnectionErrorSnackbar(message = builtInvoke.message)
                                else
                                    ConnectionErrorSnackbar()
                        }
                    }else{
                        //Show generic snackbar
                        BaseSnackbar(message = it.visuals.message)
                    }

                } }) { innerPadding ->
                    Surface(color = Background) {
                        MainScreen(modifier = Modifier.padding(innerPadding), snackbarState, window)
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, snackbarHostState: SnackbarHostState, window : Window) {
    //Define screen properties
    val navController = rememberNavController()
    val activityProperties = ActivityProperties(
        navController = navController,
        snackbarHostState = snackbarHostState,
        storage = LocalStorage(LocalContext.current),
        window = window,
        parameters = HashMap()
    )

    AppNavGraph(
        activityProperties = activityProperties,
        modifier = modifier,
        start = if((activityProperties.storage.load("auth")?:"").isNotBlank()) Screen.Home else Screen.Welcome
    )

    //Load overlay
    if((activityProperties.parameters["isLoading"]?: false as Boolean) as Boolean){
        LoadingOverlay()
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}