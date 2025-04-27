package com.asier.arguments.screens.messaging

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.inputs.ChatTextInput
import com.asier.arguments.ui.components.messaging.ChatMessageDialog
import com.asier.arguments.ui.components.others.UserAlt
import com.asier.arguments.ui.components.topbars.TitleTopBar
import com.asier.arguments.ui.theme.TopBarBackground
import kotlinx.coroutines.delay

@SuppressLint("ContextCastToActivity")
@Composable
fun MessagingScreen(messagingScreenViewModel: MessagingScreenViewModel){
    //Activity parameters vm load
    val parameters: ActivityParameters = viewModel(LocalContext.current as ComponentActivity)
    val activityProperties: ActivityProperties = parameters.properties

    val scope = rememberCoroutineScope()
    messagingScreenViewModel.storage = activityProperties.storage

    //Change status bar color
    activityProperties.window.let {
        SideEffect {
            WindowCompat.getInsetsController(it, it.decorView)
                .isAppearanceLightStatusBars = true
            it.statusBarColor = TopBarBackground.toArgb()
        }
    }

    messagingScreenViewModel.loadUsername()

    //Load new messages with little delay
    LaunchedEffect(Unit) {
        while (true){
            messagingScreenViewModel.loadMessages(scope)
            delay(2000)
        }
    }


    messagingScreenViewModel.checkDiscussionAvailability(activityProperties,scope)


    TitleTopBar(title = "XYZ",
        modifier = Modifier.fillMaxWidth()
    )

    MessageBoard(messagingScreenViewModel)
}

@Composable
fun MessageBoard(messagingScreenViewModel : MessagingScreenViewModel){
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(top = 100.dp)) {
        LazyColumn(modifier = Modifier.weight(.9f).padding(5.dp).fillMaxWidth()) {
            itemsIndexed(messagingScreenViewModel.messages
                .toSortedMap()
                .values
                .flatten()){ index, item ->
                ChatMessageDialog(
                    message = item,
                    self = item.sender == messagingScreenViewModel.username,
                    modifier = Modifier.padding(top = 10.dp),
                    userAlt = {
                        UserAlt(name = item.sender) { }
                    })
            }
        }
        ChatTextInput(
            onValueChanged = {messagingScreenViewModel.writingMessage = it},
            text = messagingScreenViewModel.writingMessage,
            modifier = Modifier.weight(.1f),
            onSendClicked = {
                messagingScreenViewModel.sendMessage(scope)
            })
    }
}