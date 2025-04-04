package com.asier.arguments.ui.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asier.arguments.ui.theme.Disabled
import com.asier.arguments.ui.theme.Secondary

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    fontWeight: FontWeight = FontWeight.Medium,
    padding: PaddingValues = PaddingValues(50.dp,5.dp)
){
    BaseButton(text = text,onClick = onClick, buttonColors = ButtonColors(
        contentColor = Color.White,
        containerColor =  Secondary,
        disabledContentColor = Color.White,
        disabledContainerColor =  Disabled),
        enabled = enabled,
        fontWeight = fontWeight,
        padding = padding,
        modifier = modifier
    )
}

@Preview
@Composable
fun SecondaryButtonPreview(){
    SecondaryButton("Secondary button", onClick = {})
}
