package com.asier.arguments

import android.graphics.Color
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.snackbars.BaseSnackbar
import com.asier.arguments.ui.components.snackbars.ConnectionErrorSnackbar
import com.asier.arguments.ui.components.snackbars.SnackbarInvoke
import com.asier.arguments.ui.components.snackbars.SnackbarType
import com.asier.arguments.ui.theme.ArgumentsTheme
import com.asier.arguments.ui.theme.Background
import com.asier.arguments.ui.theme.TopBarBackground
import com.asier.arguments.utils.storage.LocalStorage

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
        window = window
    )

    AppNavGraph(
        activityProperties = activityProperties,
        modifier = modifier,
        start = if((activityProperties.storage.load("auth")?:"").isNotBlank()) Screen.Home else Screen.Welcome
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}