package com.asier.arguments

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.screens.home.HomeScreen
import com.asier.arguments.screens.login.LoginPage
import com.asier.arguments.screens.register.RegisterSecuenceScreen
import com.asier.arguments.screens.register.RegisterSequenceViewModel
import com.asier.arguments.screens.WelcomePage
import com.asier.arguments.screens.discussions.DiscussionThreadCreationScreen
import com.asier.arguments.screens.discussions.DiscussionThreadCreationViewModel
import com.asier.arguments.screens.home.HomeScreenViewModel
import com.asier.arguments.screens.login.LoginViewModel
import com.asier.arguments.screens.messaging.MessagingScreen
import com.asier.arguments.screens.messaging.MessagingScreenViewModel
import com.asier.arguments.screens.profile.ProfileEditorScreen
import com.asier.arguments.screens.profile.ProfileEditorScreenViewModel
import com.asier.arguments.screens.profile.ProfileScreen
import com.asier.arguments.screens.profile.ProfileScreenViewModel

/**
 * Manage all the navigation flow in the application
 */
@SuppressLint("ContextCastToActivity")
@Composable
fun AppNavGraph(modifier : Modifier, start: Screen = Screen.Welcome) {
    //Activity parameters vm load
    val parameters: ActivityParameters = viewModel(LocalContext.current as ComponentActivity)

    NavHost(navController = parameters.properties.navController, startDestination = start.route, modifier = modifier){
        //Start point and user auth
        composable(Screen.Welcome.route){
            WelcomePage()
        }
        composable(Screen.Login.route){
            LoginPage(LoginViewModel())
        }
        composable(Screen.Register.route){
            RegisterSecuenceScreen(RegisterSequenceViewModel())
        }
        //Home
        composable(Screen.Home.route){
            HomeScreen(HomeScreenViewModel())
        }
        //Profiles
        composable(Screen.Profile.route){
            ProfileScreen(ProfileScreenViewModel())
        }
        composable(Screen.ProfileEdit.route){
            ProfileEditorScreen(ProfileEditorScreenViewModel())
        }
        //Discussions
        composable(Screen.DiscussionCreate.route){
            DiscussionThreadCreationScreen(DiscussionThreadCreationViewModel())
        }
        //Messaging
        composable(Screen.Messaging.route){
            MessagingScreen(MessagingScreenViewModel())
        }
    }
}