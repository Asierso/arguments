package com.asier.arguments.ui.components.inputs

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.asier.arguments.R
import com.asier.arguments.ui.theme.TextBoxText

@Composable
fun IconTextInput(
    placeholder: String,
    text: String = "",
    leadingIcon: @Composable () -> Unit,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    isPassword: Boolean = false
) {
    BaseTextInput(
        text = text,
        placeholder = placeholder,
        onValueChanged = onValueChanged,
        modifier=modifier,
        leadingIcon = leadingIcon,
        maxLines = 1,
        isError = isError,
        isPassword = isPassword
    )
}

@Composable
@Preview
fun IconTextInputPreview(){
    IconTextInput(placeholder = "Usuario", leadingIcon = {
        Icon(
            painter = painterResource(R.drawable.ic_person),
            tint = TextBoxText,
            contentDescription = null
        )
    }, onValueChanged = {})
}

@Composable
@Preview
fun IconTextInputErrorPreview(){
    IconTextInput(placeholder = "Usuario", leadingIcon = {
        Icon(
            painter = painterResource(R.drawable.ic_person),
            tint = TextBoxText,
            contentDescription = null
        )
    }, onValueChanged = {}, isError = true)
}