package com.asier.arguments.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/*
Activity parameters are all that are reachable to changes. Perfect to send plain data to ever screens
 */
class ActivityParameters : ViewModel() {
    var viewProfile by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    lateinit var properties: ActivityProperties
}