package com.asier.arguments.screens.messaging


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.Screen
import com.asier.arguments.api.discussions.DiscussionsService
import com.asier.arguments.entities.DiscussionThread
import com.asier.arguments.entities.Message
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.utils.GsonUtils
import com.asier.arguments.utils.storage.LocalStorage
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant

class MessagingScreenViewModel : ViewModel() {
    var storage by mutableStateOf<LocalStorage?>(null)
    var username by mutableStateOf("you")

    var messages = mutableStateListOf<Message>()
    var currentPage = 0

    var writingMessage by mutableStateOf("")

    fun loadUsername() {
        username = storage!!.load("user")!!
    }

    fun loadMessages() {
        messages.add(Message(text = "Lorem ipsum", author = "dummy", sendTime = Instant.now()))
        messages.add(Message(text = "Lorem ipsum", author = "you", sendTime = Instant.now()))
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
                        }
                        else -> {
                            throw Exception()
                        }
                    }
                } catch (e: Exception) {
                    //Delete discussion reference from storage and go to home screen
                    withContext(Dispatchers.Main) {
                        storage!!.delete("discussion")
                        activityProperties.navController.navigate(Screen.Home.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}