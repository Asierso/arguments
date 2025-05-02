package com.asier.arguments.ui.components.inputs

import android.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asier.arguments.R
import com.asier.arguments.ui.theme.TextBoxBorder
import com.asier.arguments.ui.theme.TextBright1

@Composable
fun ChatTextInput(
    onValueChanged: (String) -> Unit,
    onSendClicked: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Type...",
    text: String = "") {

    Box(modifier = modifier.fillMaxWidth()) {
        BaseTextInput(
            placeholder = placeholder,
            text = text,
            onValueChanged = onValueChanged,
            modifier = Modifier.fillMaxWidth().shadow(5.dp).drawBehind {
                val strokeBottom = 2.dp.toPx()
                val radius = 9.dp.toPx()
                val width = size.width
                val height = size.height-9


                drawRoundRect(
                    color = TextBoxBorder,
                    topLeft = Offset(11f, 50f),
                    size = Size(width-22f, height-strokeBottom-43f),
                    cornerRadius = CornerRadius(radius, radius)
                )
            }
        )
        IconButton(
            onClick = {
                onSendClicked()
            },
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
    ChatTextInput(onValueChanged = {}, onSendClicked = {})
}