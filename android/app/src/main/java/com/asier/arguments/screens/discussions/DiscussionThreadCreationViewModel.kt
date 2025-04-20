package com.asier.arguments.screens.discussions

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.Screen
import com.asier.arguments.api.discussions.DiscussionsService
import com.asier.arguments.entities.DiscussionCreatorDto
import com.asier.arguments.entities.DiscussionThread
import com.asier.arguments.misc.StatusCodes
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.snackbars.SnackbarInvoke
import com.asier.arguments.ui.components.snackbars.SnackbarType
import com.asier.arguments.utils.GsonUtils
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
                        //TODO Implement discussion access
                        //Get id: GsonUtils.jsonToClass<DiscussionThread>(response.result!! as LinkedTreeMap<*, *>).id

                        withContext(Dispatchers.Main){
                            activityProperties.navController.navigate(Screen.Home.route)
                        }

                        activityProperties.snackbarHostState.showSnackbar(
                            message = SnackbarInvoke(SnackbarType.SUCCESS).apply {
                                message = "Creado"
                            }.build(),
                            duration = SnackbarDuration.Short
                        )
                    }
                    else -> {
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

    fun timeType() : String{
        return if(maxTime < 5) {
            "Muy poco tiempo"
        } else if(maxTime < 10){
            "Debate corto"
        } else if(maxTime < 30){
            "Debate mediano"
        } else{
            "Debate largo"
        }
    }

    fun userType() : String{
        return if(maxUsers <= 1) {
            "No puedes autodebatir"
        } else if(maxUsers < 5){
            "Pocas personas"
        } else if(maxUsers < 10){
            "Debate estándar"
        } else{
            "Debate multitudinario"
        }
    }

    fun titleType() : String{
        return if(title.length < 5)
            "Titulo demasiado corto"
        else if(title.length > 30)
            "Titulo demasiado largo"
        else
            "¡Buen tema!"
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