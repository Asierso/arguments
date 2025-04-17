package com.asier.arguments.utils

import androidx.lifecycle.ViewModel
import com.asier.arguments.screens.ActivityProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object LoadingContext {
    /*
    fun executeAtLoading(activityProperties: ActivityProperties,scope: CoroutineScope,viewModel: ViewModel, actions: (vm : ViewModel) -> Unit){
        activityProperties.parameters.isLoading = true
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                actions.invoke(viewModel)
                delay(300)
                withContext(Dispatchers.Main) {
                    activityProperties.parameters.isLoading = false
                }
            }
        }
    }*/
}