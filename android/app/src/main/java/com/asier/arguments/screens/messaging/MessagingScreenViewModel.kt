package com.asier.arguments.screens.messaging


import android.content.Context
import android.content.Intent
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.MainActivity
import com.asier.arguments.Screen
import com.asier.arguments.api.discussions.DiscussionsService
import com.asier.arguments.api.messaging.MessagingService
import com.asier.arguments.entities.DiscussionThread
import com.asier.arguments.entities.Message
import com.asier.arguments.entities.MessageCreatorDto
import com.asier.arguments.entities.pages.PageResponse
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.snackbars.SnackbarInvoke
import com.asier.arguments.ui.components.snackbars.SnackbarType
import com.asier.arguments.utils.GsonUtils
import com.asier.arguments.utils.storage.LocalStorage
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant

class MessagingScreenViewModel : ViewModel() {
    var storage by mutableStateOf<LocalStorage?>(null)
    var username by mutableStateOf("you")

    //Discussion data
    var discussion by mutableStateOf<DiscussionThread?>(null)
    var messages = mutableStateMapOf<Int, List<Message>>()

    //Messaging page data
    var currentPage = 0
    var totalPages = 0

    //UI and load Controller
    var writingMessage by mutableStateOf("")
    var updatingCycle by mutableStateOf(false)
    var firstLoad by mutableStateOf(true)
    var shouldBeLoading by mutableStateOf(true)
    var activityRestarting by mutableStateOf(false)
    var alreadyVoted by mutableStateOf(false)

    fun loadUsername() {
        username = storage!!.load("user")!!
    }

    fun sendMessage(scope: CoroutineScope) {
        if (writingMessage.isBlank())
            return

        val id = storage?.load("discussion") ?: ""

        //Compose message and clear message input
        val messageComposed = MessageCreatorDto(sender = username, message = writingMessage)
        writingMessage = ""

        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val response = MessagingService.insertMessage(storage!!, id, messageComposed)
                when (StatusCodes.valueOf(response!!.status)) {
                    StatusCodes.SUCCESSFULLY -> {

                    }

                    else -> {}
                }
            }
        }
    }

    fun loadNextMessagesPage(parameters: ActivityParameters, scope: CoroutineScope) {
        if (currentPage < totalPages) {
            currentPage++
            loadMessages(parameters, scope, pageNum = currentPage)
        }
    }

    fun loadMessages(parameters: ActivityParameters, scope: CoroutineScope, pageNum: Int = 0) {
        val id = storage?.load("discussion") ?: ""
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                //Try to get messages
                try {
                    val response = MessagingService.getMessagesByPage(storage!!, id, pageNum)
                    when (StatusCodes.valueOf(response!!.status)) {
                        StatusCodes.SUCCESSFULLY -> {
                            //Parse response
                            val resultJson =
                                GsonUtils.gson.toJson(response.result as LinkedTreeMap<*, *>)
                            val type = object : TypeToken<PageResponse<Message>>() {}.type
                            val resultPage =
                                GsonUtils.gson.fromJson<PageResponse<Message>>(
                                    resultJson,
                                    type
                                )

                            //Load messages in the map and stop loading overlay if is in screen
                            withContext(Dispatchers.Main) {
                                messages.put(resultPage.number, resultPage.content.toMutableList())
                                totalPages = resultPage.totalPages
                                parameters.isLoading = false
                            }
                        }

                        else -> {}
                    }
                } catch (ignore: Exception) {
                    //Show loading overlay
                    withContext(Dispatchers.Main) {
                        parameters.isLoading = true
                    }
                }
            }
        }
    }

    fun checkDiscussionAvailability(parameters: ActivityParameters, scope: CoroutineScope, context: Context) {
        val id = storage?.load("discussion") ?: ""

        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    //Try to load discussion. If is expired or is another error, just leave message page
                    val response = DiscussionsService.getDiscussionById(storage!!, id)
                    when (StatusCodes.valueOf(response!!.status)) {
                        StatusCodes.SUCCESSFULLY -> {
                            val loaded =
                                GsonUtils.jsonToClass<DiscussionThread>(response.result as LinkedTreeMap<*, *>)
                            if (loaded.votingGraceAt!!.isBefore(Instant.now())) {
                                throw Exception()
                            } else {
                                discussion = loaded
                            }
                        }

                        else -> {
                            throw Exception()
                        }
                    }
                } catch (e: Exception) {
                    //Delete discussion reference from storage and go to home screen
                    withContext(Dispatchers.Main) {
                        storage!!.delete("discussion")
                        shouldBeLoading = false
                        parameters.isLoading = true
                    }

                    if(activityRestarting)
                        return@launch
                    activityRestarting = true

                    //Save flag to show expired message
                    if(storage!!.load("discussion_expired") == null)
                        storage!!.save("discussion_expired","true")

                    delay(1000)

                    //Restart activity (discussion is expired)
                    withContext(Dispatchers.Main) {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    fun refreshDiscussion(parameters: ActivityParameters, scope: CoroutineScope) {
        val id = storage?.load("discussion") ?: ""

        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    //Try to load discussion. If is expired or is another error, just leave message page
                    val response = DiscussionsService.getDiscussionById(storage!!, id)
                    when (StatusCodes.valueOf(response!!.status)) {
                        StatusCodes.SUCCESSFULLY -> {
                            withContext(Dispatchers.Main){
                                discussion = GsonUtils.jsonToClass<DiscussionThread>(response.result as LinkedTreeMap<*, *>).copy()
                            }
                        }

                        else -> {
                            throw Exception()
                        }
                    }
                } catch (ignore: Exception) {

                }
            }
        }
    }

    fun startUpdatingCycle(parameters: ActivityParameters, scope: CoroutineScope) {
        if (updatingCycle)
            return
        updatingCycle = true
        scope.launch {
            var counter = 0
            while (shouldBeLoading) {
               withContext(Dispatchers.Main){
                    loadMessages(parameters, scope)

                   //Execute refresh discussion ever 5s (to load discussion changes)
                    if(counter % 5 == 0){
                        refreshDiscussion(parameters,scope)
                    }
                    if (counter >= Int.MAX_VALUE - 10) {
                       counter = 0
                    }

                }

                delay(1000)
                counter++

                //If is loaded
                if (firstLoad) {
                    firstLoad = false
                }
            }
        }
    }

    fun vote(candidate: String,activityProperties: ActivityProperties, scope: CoroutineScope){
        //Avoid double-voting
        if(alreadyVoted)
            return

        scope.launch {
            withContext(Dispatchers.IO){
                try {
                    //Send vote request to server
                    val response = DiscussionsService.voteTo(
                        localStorage = storage!!,
                        discussionId = discussion!!.id,
                        userVote = candidate
                    )

                    when (StatusCodes.valueOf(response!!.status)) {
                        StatusCodes.SUCCESSFULLY -> {
                            withContext(Dispatchers.Main) {
                                alreadyVoted = true
                                discussion!!.votes.put(
                                    candidate,
                                    discussion!!.votes.getValue(candidate)+1
                                )
                            }
                        }

                        StatusCodes.VOTING_CLOSED -> {
                            activityProperties.snackbarHostState.showSnackbar(
                                message = SnackbarInvoke(
                                    SnackbarType.WARNING,
                                    "Votación cerrada"
                                ).build(),
                                duration = SnackbarDuration.Short
                            )
                        }

                        StatusCodes.USER_ALREADY_VOTED -> {
                            activityProperties.snackbarHostState.showSnackbar(
                                message = SnackbarInvoke(
                                    SnackbarType.WARNING,
                                    "Votación ya realizada"
                                ).build(),
                                duration = SnackbarDuration.Short
                            )
                        }

                        else -> {
                            activityProperties.snackbarHostState.showSnackbar(
                                message = SnackbarInvoke(SnackbarType.SERVER_ERROR).build(),
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }catch (e: Exception){
                    activityProperties.snackbarHostState.showSnackbar(
                        message = SnackbarInvoke(SnackbarType.SERVER_ERROR).build(),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
}