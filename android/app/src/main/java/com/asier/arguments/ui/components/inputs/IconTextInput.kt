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
    modifier: Modifier = Modifier,
    text: String = "",
    suffix: String = "",
    leadingIcon: @Composable () -> Unit,
    onValueChanged: (String) -> Unit,
    isError: Boolean = false,
    isPassword: Boolean = false,
    enabled: Boolean = true,
    upDown: OnUpDown? = null
) {
    BaseTextInput(
        placeholder = placeholder,
        modifier=modifier,
        onValueChanged = onValueChanged,
        text = text,
        suffix = suffix,
        leadingIcon = leadingIcon,
        isError = isError,
        enabled = enabled,
        isPassword = isPassword,
        upDown = upDown
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