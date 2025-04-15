package com.asier.arguments.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.utils.storage.LocalStorage

class HomeScreenViewModel : ViewModel() {
    var storage by mutableStateOf<LocalStorage?>(null)
    var username by mutableStateOf("")
    var loaded by mutableStateOf(false)

    fun loadUsername(){
        if(username.isNotBlank())
            return

        storage?.load("user")?.let {
                username = it
        }
    }
}