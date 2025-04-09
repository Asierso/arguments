package com.asier.arguments.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegisterSecuenceViewModel : ViewModel() {
    //Plain user data
    var firstname by mutableStateOf("")
    var lastname by mutableStateOf("")
    var username by mutableStateOf("")
    var password1 by mutableStateOf("")
    var password2 by mutableStateOf("")

    //Register sequence flow data
    var step by mutableIntStateOf(0)
    var uniqueUsername by mutableStateOf(true)
    var uniqueTry by mutableStateOf(true)
}