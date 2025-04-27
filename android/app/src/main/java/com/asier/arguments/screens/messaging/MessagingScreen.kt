package com.asier.arguments.screens.messaging

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.launch

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

    messagingScreenViewModel.checkDiscussionAvailability(activityProperties,scope)

    //Load new messages with little delay
    messagingScreenViewModel.startUpdatingCycle(activityProperties,scope)

    TitleTopBar(title = messagingScreenViewModel.discussionTitle,
        modifier = Modifier.fillMaxWidth()
    )

    MessageBoard(messagingScreenViewModel)
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun MessageBoard(messagingScreenViewModel : MessagingScreenViewModel){
    val scope = rememberCoroutineScope()

    //List handlers
    val listState = rememberLazyListState()
    val messageListSize = messagingScreenViewModel.messages.toSortedMap().values.flatten().size
    val isAtTop = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
    }

    if (isAtTop.value) {
        //TODO: Implement top loading
    }


    LaunchedEffect(messageListSize) {
        if(messageListSize > 0)
            listState.animateScrollToItem( messageListSize - 1)
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = 100.dp)) {
        LazyColumn(
            modifier = Modifier.weight(.9f).padding(5.dp).fillMaxWidth(),
            state = listState
        ) {
            itemsIndexed(messagingScreenViewModel.messages
                .toSortedMap()
                .values
                .flatten()
                .sortedBy {
                    it.sendTime
                }){ index, item ->
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

