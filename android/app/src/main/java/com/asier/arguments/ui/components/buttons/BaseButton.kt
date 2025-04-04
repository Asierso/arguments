package com.asier.arguments.ui.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright0

@Composable
fun BaseButton(
    text: String,
    onClick: () -> Unit,
    buttonColors : ButtonColors,
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    fontWeight: FontWeight = FontWeight.Medium,
    padding : PaddingValues = PaddingValues(50.dp,5.dp)
){
    Button(modifier = modifier,
        colors = buttonColors,
        shape = RoundedCornerShape(10.dp),
        contentPadding = padding,
        enabled = enabled,
        onClick = {
            onClick()
        }) {
        Text(text = text, fontFamily = Montserrat, fontWeight = fontWeight, color = TextBright0)
    }
}

@Preview
@Composable
fun BaseButtonPreview(){
    BaseButton("Button", onClick = {}, buttonColors = ButtonDefaults.buttonColors())
}