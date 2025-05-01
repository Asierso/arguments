package com.asier.arguments.screens.home

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.Screen
import com.asier.arguments.api.AuthFacade
import com.asier.arguments.api.discussions.DiscussionsService
import com.asier.arguments.api.login.LoginService
import com.asier.arguments.api.users.UsersService
import com.asier.arguments.entities.DiscussionThread
import com.asier.arguments.entities.DiscussionUserPairDto
import com.asier.arguments.entities.pages.PageResponse
import com.asier.arguments.entities.user.User
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.snackbars.SnackbarInvoke
import com.asier.arguments.ui.components.snackbars.SnackbarType
import com.asier.arguments.utils.GsonUtils
import com.asier.arguments.utils.storage.LocalStorage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.time.LocalDateTime

class HomeScreenViewModel : ViewModel() {
    //Inherited elements
    var storage by mutableStateOf<LocalStorage?>(null)
    var username by mutableStateOf("")

    var lazyList = LazyListState()

    //Loading flags
    var pageLoading by mutableStateOf(false)
    var pageRefreshing by mutableStateOf(false)

    var loadedDiscussions = mutableStateListOf<DiscussionUserPairDto>()

    //Pagination info
    var page by mutableIntStateOf(0)
    var totalElements by mutableLongStateOf(0)
    var totalPages by mutableIntStateOf(1)

    //Alert dialogs handlers
    var discussionWarning by mutableStateOf(false)
    var discussionPreloaded by mutableStateOf("")
    var discussionExpiredWarning by mutableStateOf(false)

    fun loadUsername() {
        if (username.isNotBlank())
            return

        storage?.load("user")?.let {
            username = it
        }
    }

    fun openDiscussion(activityProperties: ActivityProperties,scope: CoroutineScope){
        scope.launch {
            withContext(Dispatchers.IO){
                val response = DiscussionsService.joinDiscussionById(storage!!,discussionPreloaded)
                when(StatusCodes.valueOf(response!!.status)){
                    StatusCodes.SUCCESSFULLY -> {
                        //Open discussion and save got id
                        withContext(Dispatchers.Main){
                            storage?.save("discussion",discussionPreloaded)
                            activityProperties.navController.navigate(Screen.Messaging.route)
                        }
                    }
                    StatusCodes.EXPIRED_DISCUSSION -> {
                        activityProperties.snackbarHostState.showSnackbar(
                            message = SnackbarInvoke(SnackbarType.SERVER_ERROR,"Discusion expirada").build(),
                            duration = SnackbarDuration.Short
                        )
                    }
                    StatusCodes.DISCUSSION_MAX_REACHED -> {
                        activityProperties.snackbarHostState.showSnackbar(
                            message = SnackbarInvoke(SnackbarType.WARNING,"Discusion llena").build(),
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
            }
        }
    }

    fun loadProfile(activityProperties: ActivityProperties) {
        activityProperties.navController.navigate(Screen.Profile.route)
        pageRefreshing = true
        pageLoading = false
        page = 0
        loadedDiscussions.clear()
    }

    fun reloadDiscussionsPage(parameters: ActivityParameters, scope: CoroutineScope) {
        pageRefreshing = true
        pageLoading = false
        page = 0

        loadedDiscussions.clear()

        loadNextDiscussionsPage(parameters, scope)
    }

    fun loadNextDiscussionsPage(parameters: ActivityParameters, scope: CoroutineScope) {
        //Stop loading if is loading or page limit reached
        if (pageLoading || page >= totalPages)
            return

        pageLoading = true

        val activityProperties = parameters.properties

        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                //If there's no bearer token, omit load (avoid null pointer at logout)
                if (storage!!.load("auth") != null) {

                    try {
                        val result = DiscussionsService.getDiscussionsByPage(storage!!, page)

                        when (result?.status?.let { StatusCodes.valueOf(it) }) {
                            StatusCodes.UNAUTHORIZED_AUTH -> {
                                storage!!.delete("auth")
                                storage!!.delete("user")
                                withContext(Dispatchers.Main) {
                                    activityProperties.navController.navigate(Screen.Login.route)
                                }
                            }

                            else -> {}
                        }

                        if (result?.status?.let { StatusCodes.valueOf(it) } == StatusCodes.SUCCESSFULLY) {
                            //Parse pagination to get discussions
                            val resultJson =
                                GsonUtils.gson.toJson(result.result as LinkedTreeMap<*, *>)
                            val type = object : TypeToken<PageResponse<DiscussionThread>>() {}.type
                            val resultPage =
                                GsonUtils.gson.fromJson<PageResponse<DiscussionThread>>(
                                    resultJson,
                                    type
                                )

                            val resultMapped = resultPage.content.map {
                                return@map DiscussionUserPairDto(it, loadUserDetails(scope, it))
                            }

                            //Update states
                            withContext(Dispatchers.Main) {
                                totalElements = resultPage.totalElements
                                totalPages = resultPage.totalPages
                                page += 1

                                //Map DiscussionThrad to DiscussionUserPair. This is because user data will be loaded when is shown
                                loadedDiscussions.addAll(resultMapped)

                                pageLoading = false
                                pageRefreshing = false
                                parameters.isLoading = false

                                Log.d("debug", "Page ${page} of ${totalPages}")
                            }
                        }
                    }catch (e: Exception){
                        delay(2000)

                        //Try to load again
                        withContext(Dispatchers.Main){
                            parameters.isLoading = true
                            pageLoading = false
                            pageRefreshing = false
                            loadNextDiscussionsPage(parameters,scope)
                        }
                    }
                }
            }
        }
    }

    suspend fun loadUserDetails(scope: CoroutineScope, discussionThread: DiscussionThread) : User? {
        val response = UsersService.getByUsername(discussionThread.author)

        if (StatusCodes.valueOf(response!!.status) == StatusCodes.SUCCESSFULLY) {
            val userData = GsonUtils.jsonToClass<User>(response.result as LinkedTreeMap<*,*>)

            return userData
        }

        return null
    }

    fun logout(activityProperties: ActivityProperties, scope: CoroutineScope) {
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                AuthFacade.logout(activityProperties)
            }
        }
    }
}