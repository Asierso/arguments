package com.asier.arguments.screens.home

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.Screen
import com.asier.arguments.api.discussions.DiscussionsService
import com.asier.arguments.entities.DiscussionThread
import com.asier.arguments.entities.pages.PageResponse
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.utils.storage.LocalStorage
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class HomeScreenViewModel : ViewModel() {
    //Inherited elements
    var storage by mutableStateOf<LocalStorage?>(null)
    var username by mutableStateOf("")

    var lazyList = LazyListState()

    //Loading flags
    var firstLoaded by mutableStateOf(false)
    var pageLoading by mutableStateOf(false)
    var pageRefreshing by mutableStateOf(false)

    var loadedDiscussions = mutableStateListOf<DiscussionThread>()

    //Pagination info
    var page by mutableIntStateOf(0)
    var totalElements by mutableLongStateOf(0)
    var totalPages by mutableIntStateOf(1)

    fun loadUsername(){
        if(username.isNotBlank())
            return

        storage?.load("user")?.let {
            username = it
        }
    }

    fun loadSelfProfile(activityProperties: ActivityProperties){
        activityProperties.navController.navigate(Screen.Profile.route)
        pageRefreshing = true
        pageLoading = false
        page = 0
        loadedDiscussions.clear()
    }

    fun reloadDiscussionsPage(scope: CoroutineScope){
        pageRefreshing = true
        pageLoading = false
        page = 0
        loadedDiscussions.clear()
        loadNextDiscussionsPage(scope)
    }

    fun loadNextDiscussionsPage(scope: CoroutineScope){
        //Stop loading if is loading or page limit reached
        if(pageLoading || page >= totalPages)
            return

        pageLoading = true
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val result = DiscussionsService.getDiscussionsByPage(storage!!,page)

                if(result?.status?.let { StatusCodes.valueOf(it) } == StatusCodes.SUCCESSFULLY){
                    //Parse pagination to get discussions
                    val gson =  GsonBuilder()
                        .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
                            LocalDateTime.parse(json.asString)
                        })
                        .create()
                    val resultJson = gson.toJson(result.result as LinkedTreeMap<*,*>)
                    val type = object : TypeToken<PageResponse<DiscussionThread>>() {}.type
                    val resultPage = gson.fromJson<PageResponse<DiscussionThread>>(resultJson, type)

                    //Update states
                    withContext(Dispatchers.Main) {
                        totalElements = resultPage.totalElements
                        totalPages = resultPage.totalPages
                        page += 1
                        loadedDiscussions.addAll(resultPage.content)

                        pageLoading = false
                        pageRefreshing = false
                        Log.d("debug","Page ${page} of ${totalPages}")
                    }
                }
            }
        }
    }
}