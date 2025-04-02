package com.asier.arguments.ui.components.buttons

import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.asier.arguments.ui.theme.Disabled
import com.asier.arguments.ui.theme.Secondary

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    fontWeight: FontWeight = FontWeight.Medium){
    BaseButton(text = text,onClick = onClick, buttonColors = ButtonColors(
        contentColor = Color.White,
        containerColor =  Secondary,
        disabledContentColor = Color.White,
        disabledContainerColor =  Disabled),
        enabled = enabled,
        fontWeight = fontWeight
    )
}

@Preview
@Composable
fun SecondaryButtonPreview(){
    SecondaryButton("Secondary button", onClick = {})
}
