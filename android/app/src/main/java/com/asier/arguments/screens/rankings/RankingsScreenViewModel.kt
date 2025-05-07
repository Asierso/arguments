package com.asier.arguments.screens.rankings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.asier.arguments.utils.storage.LocalStorage

class RankingsScreenViewModel : ViewModel() {
    var storage by mutableStateOf<LocalStorage?>(null)
}