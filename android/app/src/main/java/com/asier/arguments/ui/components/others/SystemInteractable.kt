package com.asier.arguments.ui.components.others

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext

//Hide keyboard on invoke
fun hideKeyboard(context: Context) {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusView = (context as? Activity)?.currentFocus
    inputMethodManager.hideSoftInputFromWindow(currentFocusView?.windowToken, 0)
}

//Extension of Modifier to allow hide keyboard when click is detected
@Composable
fun Modifier.hideKeyboardOnClick(): Modifier {
    val context = LocalContext.current
    return this.pointerInput(Unit) {
        detectTapGestures {
           hideKeyboard(context)
        }
    }
}