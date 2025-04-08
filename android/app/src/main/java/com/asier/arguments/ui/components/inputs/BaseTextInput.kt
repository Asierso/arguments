package com.asier.arguments.ui.components.inputs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBoxBackground
import com.asier.arguments.ui.theme.TextBoxBorder
import com.asier.arguments.ui.theme.TextBoxErrorTextColor0
import com.asier.arguments.ui.theme.TextBoxErrorTextColor1
import com.asier.arguments.ui.theme.TextBoxFocusedTextColor
import com.asier.arguments.ui.theme.TextBoxTextColor

@Composable
fun BaseTextInput(
    placeholder: String,
    onValueChanged: (String) -> Unit,
    text : String = "",
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    readOnly : Boolean = false,
    minLines : Int = 1,
    maxLines : Int = 1,
    isError: Boolean = false
){

    //Set common text input props
    val textPlaceholder = @Composable {
        Text(text = placeholder, color = TextBoxTextColor, fontFamily = Montserrat, fontWeight = FontWeight.Medium)
    }
    val textModifier = modifier
        .padding(4.dp)
        .border(1.dp, TextBoxBorder, RoundedCornerShape(10.dp))
    val textColors = TextFieldDefaults.colors(
        focusedContainerColor = TextBoxBackground,
        unfocusedContainerColor = TextBoxBackground,
        errorContainerColor = TextBoxBackground,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        focusedLeadingIconColor = TextBoxFocusedTextColor,
        unfocusedLeadingIconColor = TextBoxBorder,
        focusedTextColor = TextBoxFocusedTextColor,
        unfocusedTextColor = TextBoxBorder,
        errorPlaceholderColor = TextBoxErrorTextColor1,
        errorTextColor =  TextBoxErrorTextColor0
    )

    if(leadingIcon == null){
        TextField(
            value = text,
            onValueChange = onValueChanged,
            placeholder = textPlaceholder,
            modifier = textModifier,
            shape = RoundedCornerShape(10.dp),
            colors = textColors,
            readOnly = readOnly,
            singleLine = minLines <= 1,
            minLines = minLines,
            isError = isError
        )
    }else{
        TextField(
            value = text,
            onValueChange = onValueChanged,
            placeholder = textPlaceholder,
            modifier = textModifier,
            shape = RoundedCornerShape(10.dp),
            colors = textColors,
            leadingIcon = {
                leadingIcon.invoke()
            },
            readOnly = readOnly,
            singleLine = minLines <= 1,
            minLines = minLines,
            isError = isError
        )
    }
}

@Composable
@Preview
fun BaseTextInputPreview(){
    BaseTextInput(placeholder = "Base", onValueChanged = {}, minLines = 1)
}