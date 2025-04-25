package com.asier.arguments.ui.components.inputs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asier.arguments.R
import com.asier.arguments.ui.theme.TextBright1

@Composable
fun ChatTextInput(
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Type...",
    text: String = "") {

    Box(modifier = modifier) {
        BaseTextInput(
            placeholder = placeholder,
            text = text,
            onValueChanged = onValueChanged,
            modifier = Modifier.shadow(5.dp)
        )
        IconButton(
            onClick = {},
            modifier = Modifier.padding(end = 15.dp).align(Alignment.CenterEnd).width(25.dp).height(25.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_send),
                contentDescription = "send",
                tint = TextBright1)
        }
    }

}

@Preview
@Composable
fun ChatTextInputPreview(){
    ChatTextInput(onValueChanged = {})
}