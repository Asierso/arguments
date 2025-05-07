package com.asier.arguments.screens.discussions

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.R
import com.asier.arguments.Screen
import com.asier.arguments.api.discussions.DiscussionsService
import com.asier.arguments.entities.DiscussionCreatorDto
import com.asier.arguments.entities.DiscussionThread
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.snackbars.SnackbarInvoke
import com.asier.arguments.ui.components.snackbars.SnackbarType
import com.asier.arguments.utils.GsonUtils
import com.asier.arguments.utils.storage.LocalStorage
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscussionThreadCreationViewModel : ViewModel() {
    var maxTime by mutableIntStateOf(-1)
    var maxUsers by mutableIntStateOf(-1)
    var title by mutableStateOf("")
    var titleModified by mutableStateOf(false)

    //Inherited elements
    var storage by mutableStateOf<LocalStorage?>(null)

    fun createDiscussion(activityProperties: ActivityProperties, scope: CoroutineScope){
        if(!userPolicy() || !timePolicy() || !titlePolicy()){
            titleModified = true
            maxTime = if(maxTime >= 0) maxTime else 0
            maxUsers = if(maxUsers >= 0) maxUsers else 0
            return
        }

        val discussion = DiscussionCreatorDto(
            title = title,
            maxUsers = maxUsers,
            duration = maxTime
            )

        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val response = DiscussionsService.createDiscussion(discussion = discussion,
                    localStorage = activityProperties.storage)

                when(StatusCodes.valueOf(response!!.status)){
                    StatusCodes.SUCCESSFULLY -> {
                        //Save discussion id and open it
                        val id = GsonUtils.jsonToClass<DiscussionThread>(response.result!! as LinkedTreeMap<*, *>).id
                        storage!!.save("discussion",id)

                        withContext(Dispatchers.Main){
                            activityProperties.navController.navigate(Screen.Messaging.route)
                        }
                    }
                    else -> {
                        //Show server error
                        activityProperties.snackbarHostState.showSnackbar(
                            message = SnackbarInvoke(SnackbarType.SERVER_ERROR).build(),
                            duration = SnackbarDuration.Short
                        )
                        activityProperties.navController.navigate(Screen.Home.route)
                    }
                }
            }
        }
    }

    fun timeType(context: Context) : String{
        return if(maxTime < 5) {
            context.getString(R.string.discussion_creation_maxtimepolicy_tooshort)
        } else if(maxTime < 10){
            context.getString(R.string.discussion_creation_maxtimepolicy_short)
        } else if(maxTime < 30){
            context.getString(R.string.discussion_creation_maxtimepolicy_mid)
        } else{
            context.getString(R.string.discussion_creation_maxtimepolicy_large)
        }
    }

    fun userType(context: Context) : String{
        return if(maxUsers <= 1) {
            context.getString(R.string.discussion_creation_maxuserspolicy_toofew)
        } else if(maxUsers < 5){
            context.getString(R.string.discussion_creation_maxuserspolicy_few)
        } else if(maxUsers < 10){
            context.getString(R.string.discussion_creation_maxuserspolicy_standart)
        } else{
            context.getString(R.string.discussion_creation_maxuserspolicy_standart2)
        }
    }

    fun titleType(context: Context) : String{
        return if(title.trim().length < 5)
            context.getString(R.string.discussion_creation_titlepolicy_tooshort)
        else if(title.trim().length > 30)
            context.getString(R.string.discussion_creation_titlepolicy_toolarge)
        else
            context.getString(R.string.discussion_creation_titlepolicy_ok)
    }

    fun userPolicy() : Boolean{
        return maxUsers >= 2
    }

    fun timePolicy() : Boolean{
        return maxTime >= 5
    }

    fun titlePolicy() : Boolean{
        return title.length >= 5 && title.length <= 30
    }
}