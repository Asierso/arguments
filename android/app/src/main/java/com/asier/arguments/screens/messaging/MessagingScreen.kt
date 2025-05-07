package com.asier.arguments.screens.messaging

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asier.arguments.R
import com.asier.arguments.entities.DiscussionThread
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.backgrounds.ArgumentsPatternBackground
import com.asier.arguments.ui.components.inputs.ChatTextInput
import com.asier.arguments.ui.components.messaging.ChatMessageDialog
import com.asier.arguments.ui.components.others.UserAlt
import com.asier.arguments.ui.components.others.VotingCard
import com.asier.arguments.ui.components.topbars.ChatTopBar
import com.asier.arguments.ui.theme.TopBarBackground
import com.asier.arguments.ui.theme.TopBarIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant

@SuppressLint("ContextCastToActivity")
@Composable
fun MessagingScreen(messagingScreenViewModel: MessagingScreenViewModel) {
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

    ArgumentsPatternBackground(alpha = .05f, modifier = Modifier
        .fillMaxSize()
        .padding(5.dp))

    messagingScreenViewModel.loadUsername()

    messagingScreenViewModel.checkDiscussionAvailability(scope, LocalContext.current)

    //Load new messages with little delay
    messagingScreenViewModel.startUpdatingCycle(parameters, scope)

    ChatTopBar(
        discussion = messagingScreenViewModel.discussion ?: DiscussionThread(),
        leadingIcon = {
            Icon(
                painterResource(R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = it,
                tint = TopBarIcon
            )
        },
        modifier = Modifier.fillMaxWidth()
    )

    MessageBoard(messagingScreenViewModel,
        whenTopReached = {
            messagingScreenViewModel.loadNextMessagesPage(parameters, scope)
        },
        onCandidateVote = {
            messagingScreenViewModel.vote(it.first,activityProperties,scope)
        },
        onVotationOpening = {
            messagingScreenViewModel.checkIfAlone(parameters)
        },
        onAltClick = {
            parameters.viewProfile = it
            messagingScreenViewModel.loadProfile(activityProperties)
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun MessageBoard(
    messagingScreenViewModel: MessagingScreenViewModel,
    whenTopReached: () -> Unit,
    onCandidateVote: (candidate: Pair<String,Int>) -> Unit,
    onVotationOpening: () -> Unit,
    onAltClick: (String) -> Unit
) {
    val scope = rememberCoroutineScope()

    //For text input and voting card updating
    val currentTime = remember { mutableStateOf(Instant.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = Instant.now()
            delay(1000)
        }
    }

    //List handlers
    val listState = rememberLazyListState()
    val messageListSize = messagingScreenViewModel.messages.toSortedMap().values.flatten().size
    val isAtTop = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
    }

    //Top trigger
    if (isAtTop.value) {
        whenTopReached()
    }

    //Check if is near to bottom to scroll down automatically
    val isNearBottom by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index!! >= messageListSize - 5
        }
    }

    //Scroll down if message list is updated, and user isn´t scrolling up
    LaunchedEffect(messageListSize) {
        if (messageListSize > 0 && (isNearBottom || messagingScreenViewModel.firstLoad)) {
            listState.animateScrollToItem(messageListSize - 1)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 95.dp)) {
        LazyColumn(
            modifier = Modifier
                .weight(.9f)
                .padding(5.dp)
                .fillMaxWidth(),
            state = listState
        ) {
            itemsIndexed(messagingScreenViewModel.messages
                .toSortedMap()
                .values
                .flatten()
                .sortedBy {
                    it.sendTime
                }) { index, item ->
                ChatMessageDialog(
                    message = item,
                    self = item.sender == messagingScreenViewModel.username,
                    modifier = Modifier.padding(top = 10.dp),
                    userAlt = {
                        UserAlt(name = item.sender) {
                            onAltClick(item.sender)
                        }
                    })
            }
        }

        //Show text input or voting card depending if discussion is active or is in voting time
        if (messagingScreenViewModel.discussion != null &&
            currentTime.value.isAfter(messagingScreenViewModel.discussion!!.endAt)) {

            //Avoid executing opening if user connects after discussion ends (for users that leaves the discussion)
            if(currentTime.value.isBefore(messagingScreenViewModel.discussion!!.votingGraceAt)) {
                onVotationOpening()
            }

            VotingCard(
                scoreboard = messagingScreenViewModel.discussion!!.votes,
                modifier = Modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp),
                onCandidateClick = {
                    if(messagingScreenViewModel.alreadyVoted){
                        return@VotingCard false
                    }
                    onCandidateVote(it)
                    return@VotingCard true
                },
                paimonVote = messagingScreenViewModel.discussion!!.paimonVote,
                endVoting = messagingScreenViewModel.discussion!!.votingGraceAt
            )
        } else {
            ChatTextInput(
                onValueChanged = { messagingScreenViewModel.writingMessage = it },
                text = messagingScreenViewModel.writingMessage,
                onSendClicked = {
                    messagingScreenViewModel.sendMessage(scope)

                    //Scroll to bottom when button is pressed
                    scope.launch {
                        if (messageListSize > 0) {
                            listState.animateScrollToItem(messageListSize - 1)
                        }
                    }
                }
            )
        }
    }
}

