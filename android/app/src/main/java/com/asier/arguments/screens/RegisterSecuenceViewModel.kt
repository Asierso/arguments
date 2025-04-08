package com.asier.arguments.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegisterSecuenceViewModel : ViewModel() {
    var step by mutableIntStateOf(0)
    var firstname by mutableStateOf("")
    var lastname by mutableStateOf("")
    var username by mutableStateOf("")
}