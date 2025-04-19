package com.asier.arguments.ui.components.inputs

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.asier.arguments.R
import com.asier.arguments.ui.components.others.hideKeyboard
import com.asier.arguments.ui.components.others.hideKeyboardOnClick
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBoxBackground
import com.asier.arguments.ui.theme.TextBoxBorder
import com.asier.arguments.ui.theme.TextError0
import com.asier.arguments.ui.theme.TextError1
import com.asier.arguments.ui.theme.TextBoxFocusedText
import com.asier.arguments.ui.theme.TextBoxText

@Composable
fun BaseTextInput(
    placeholder: String,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit,
    text: String = "",
    leadingIcon: @Composable() (() -> Unit)? = null,
    readOnly: Boolean = false,
    minLines: Int = 1,
    isError: Boolean = false,
    enabled: Boolean = true,
    isPassword: Boolean = false,
    upDown: OnUpDown? = null
) {

    //Set common text input props
    val textPlaceholder = @Composable {
        Text(
            text = placeholder,
            color = TextBoxText,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Medium
        )
    }
    val textModifier = modifier
        .padding(4.dp)
        .border(1.dp, if (isError) TextError0 else TextBoxBorder, RoundedCornerShape(10.dp))
    val textColors = TextFieldDefaults.colors(
        focusedContainerColor = TextBoxBackground,
        unfocusedContainerColor = TextBoxBackground,
        errorContainerColor = TextBoxBackground,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        focusedLeadingIconColor = TextBoxFocusedText,
        unfocusedLeadingIconColor = TextBoxFocusedText,
        focusedTextColor = TextBoxFocusedText,
        unfocusedTextColor = TextBoxFocusedText,
        errorPlaceholderColor = TextError1,
        errorTextColor = TextError0,
        disabledTextColor = TextBoxBorder,
        disabledIndicatorColor = TextBoxBorder
    )

    val focusManager = LocalFocusManager.current

    Box {
        if (leadingIcon == null) {
            TextField(
                value = text,
                onValueChange = onValueChanged,
                placeholder = textPlaceholder,

                //If there is a UpDown added, hide keyboard at press
                modifier = if (upDown != null) textModifier.onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        focusManager.clearFocus()
                    }
                } else textModifier,
                keyboardOptions = if (upDown != null) KeyboardOptions.Default.copy(imeAction = ImeAction.None) else KeyboardOptions.Default,

                shape = RoundedCornerShape(10.dp),
                colors = textColors,
                readOnly = readOnly,
                singleLine = minLines <= 1,
                minLines = minLines,
                isError = isError,
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
            )
        } else {
            TextField(
                value = text,
                onValueChange = onValueChanged,
                placeholder = textPlaceholder,

                //If there is a UpDown added, hide keyboard at press
                modifier = if (upDown != null) textModifier.onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        focusManager.clearFocus()
                    }
                } else textModifier,
                keyboardOptions = if (upDown != null) KeyboardOptions.Default.copy(imeAction = ImeAction.None) else KeyboardOptions.Default,

                shape = RoundedCornerShape(10.dp),
                colors = textColors,
                leadingIcon = {
                    leadingIcon.invoke()
                },
                readOnly = readOnly,
                singleLine = minLines <= 1,
                minLines = minLines,
                isError = isError,
                enabled = enabled,
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
            )
        }
        if (upDown != null) {
            NumericUpDown(
                onUp = {
                    upDown.onUp((if(text.isBlank()) 0 else text.toInt()) + 1)
                },
                onDown = {
                    upDown.onDown((if(text.isBlank()) 0 else text.toInt()) - 1)
                },
                modifier = Modifier
                    .zIndex(1f)
                    .align(Alignment.CenterEnd).padding(end = 12.dp, top = 3.dp)
            )
        }
    }

}

@Composable
private fun NumericUpDown(
    onUp: () -> Unit,
    onDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        IconButton(onClick = onUp, modifier = Modifier.size(25.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_d_up),
                contentDescription = "+",
                tint = TextBoxFocusedText,
                modifier = Modifier.size(30.dp)
            )
        }
        IconButton(onClick = onDown, modifier = Modifier.size(25.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_d_down),
                contentDescription = "-",
                tint = TextBoxFocusedText,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
@Preview
private fun NumericUpDownPreview() {
    NumericUpDown({}, {})
}

@Composable
@Preview
fun BaseTextInputPreview() {
    BaseTextInput(placeholder = "Base", onValueChanged = {}, minLines = 1)
}

@Composable
@Preview
fun BaseTextInputErrorPreview() {
    BaseTextInput(placeholder = "Base", onValueChanged = {}, minLines = 1, isError = true)
}

@Composable
@Preview
fun BaseTextInputDisabledPreview() {
    BaseTextInput(placeholder = "Base", onValueChanged = {}, minLines = 1, enabled = false)
}

@Composable
@Preview
fun BaseTextInputNumericPreview() {
    BaseTextInput(
        placeholder = "Base",
        onValueChanged = {},
        minLines = 1,
        enabled = false,
        upDown = object:OnUpDown{
            override fun onUp(current: Int) {
                TODO("Not yet implemented")
            }

            override fun onDown(current: Int) {
                TODO("Not yet implemented")
            }

        }
    )
}