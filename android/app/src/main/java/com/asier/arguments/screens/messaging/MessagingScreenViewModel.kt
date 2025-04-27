package com.asier.arguments.screens.messaging


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.Screen
import com.asier.arguments.api.discussions.DiscussionsService
import com.asier.arguments.api.messaging.MessagingService
import com.asier.arguments.entities.DiscussionThread
import com.asier.arguments.entities.Message
import com.asier.arguments.entities.MessageCreatorDto
import com.asier.arguments.entities.pages.PageResponse
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.screens.ActivityProperties
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
    var discussionTitle by mutableStateOf("")
    var messages = mutableStateMapOf<Int,List<Message>>()

    //Messaging page data
    var currentPage = 0
    var totalPages = 0

    var writingMessage by mutableStateOf("")

    var updatingCycle by mutableStateOf(false)

    fun loadUsername() {
        username = storage!!.load("user")!!
    }

    fun sendMessage(scope: CoroutineScope){
        if(writingMessage.isBlank())
            return


        val id = storage?.load("discussion") ?: ""

        //Compose message and clear message input
        val messageComposed = MessageCreatorDto(sender = username, message = writingMessage)
        writingMessage = ""

        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val response = MessagingService.insertMessage(storage!!,id,messageComposed)
                when(StatusCodes.valueOf(response!!.status)) {
                    StatusCodes.SUCCESSFULLY -> {

                    }
                    else -> {}
                }
            }
        }
    }

    fun loadMessages(activityProperties: ActivityProperties,scope: CoroutineScope, pageNum: Int = 0) {
        val id = storage?.load("discussion") ?: ""
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                //Try to get messages
                try {
                    val response = MessagingService.getMessagesByPage(storage!!, id, pageNum)
                    when (StatusCodes.valueOf(response!!.status)) {
                        StatusCodes.SUCCESSFULLY -> {
                            val resultJson =
                                GsonUtils.gson.toJson(response.result as LinkedTreeMap<*, *>)
                            val type = object : TypeToken<PageResponse<Message>>() {}.type
                            val resultPage =
                                GsonUtils.gson.fromJson<PageResponse<Message>>(
                                    resultJson,
                                    type
                                )

                            withContext(Dispatchers.Main) {
                                messages.put(resultPage.number, resultPage.content.toMutableList())
                                totalPages = resultPage.totalPages
                            }
                        }
                        else -> {}
                    }
                }
                catch (ignore: Exception){
                    //Error in discussion, navigate to home
                    activityProperties.navController.navigate(Screen.Home.route){
                        popUpTo(0) {inclusive = true}
                    }
                }
            }
        }
    }

    fun checkDiscussionAvailability(activityProperties: ActivityProperties, scope: CoroutineScope) {
        val id = storage?.load("discussion") ?: ""

        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    //Try to load discussion. If is expired or is another error, just leave message page
                    val response = DiscussionsService.getDiscussionById(storage!!, id)
                    when (StatusCodes.valueOf(response!!.status)) {
                        StatusCodes.SUCCESSFULLY -> {
                            val discussion =
                                GsonUtils.jsonToClass<DiscussionThread>(response.result as LinkedTreeMap<*, *>)
                            if (discussion.endAt!!.isBefore(Instant.now())) {
                                throw Exception()
                            }
                            else{
                                discussionTitle = discussion.title
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
                    }
                }
            }
        }
    }

    fun startUpdatingCycle(activityProperties: ActivityProperties,scope: CoroutineScope){
        if(updatingCycle)
            return
        updatingCycle = true
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                while(true){
                    withContext(Dispatchers.Main){
                        loadMessages(activityProperties,scope)
                    }
                    delay(1000)
                }
            }
        }
    }
}