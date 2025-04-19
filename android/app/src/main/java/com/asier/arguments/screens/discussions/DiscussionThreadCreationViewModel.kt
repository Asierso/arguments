package com.asier.arguments.screens.discussions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class DiscussionThreadCreationViewModel : ViewModel() {
    var maxTime by mutableIntStateOf(-1)
    var maxUsers by mutableIntStateOf(-1)
}