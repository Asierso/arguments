package com.asier.arguments.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.utils.storage.LocalStorage

class ProfileScreenViewModel : ViewModel() {
    //Inherited elements
    var storage by mutableStateOf<LocalStorage?>(null)
    var username by mutableStateOf("")

    fun loadUsername(){
        if(username.isNotBlank())
            return

        storage?.load("user")?.let {
            username = it
        }
    }
}