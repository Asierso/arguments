package com.asier.arguments.ui.components.inputs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asier.arguments.R
import com.asier.arguments.ui.theme.TextBoxBackground
import com.asier.arguments.ui.theme.TextBoxBorder
import com.asier.arguments.ui.theme.TextBoxFocusedTextColor
import com.asier.arguments.ui.theme.TextBoxTextColor

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
            tint = TextBoxTextColor,
            contentDescription = null
        )
    }, onValueChanged = {})
}