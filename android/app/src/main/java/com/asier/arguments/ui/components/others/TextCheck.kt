package com.asier.arguments.ui.components.others

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asier.arguments.R
import com.asier.arguments.ui.theme.TextCorrect0
import com.asier.arguments.ui.theme.TextError0

@Composable
fun TextCheck(
    isCorrect: Boolean,
    reason: String,
    modifier: Modifier = Modifier,
    size: Float = 15f) {
    Row(modifier = modifier) {
        Icon(
            painter = painterResource(if(isCorrect) R.drawable.ic_check else R.drawable.ic_error),
            contentDescription = reason,
            modifier = modifier.height(Dp(value = size+3)).width(Dp(value = size+3)),
            tint = if(isCorrect) TextCorrect0 else TextError0)
        Text(
            text = reason,
            fontSize = size.sp,
            fontWeight = FontWeight.Medium,
            color = if(isCorrect) TextCorrect0 else TextError0,
            modifier = Modifier.align(alignment = Alignment.CenterVertically).padding(start = 4.dp) )
    }
}

@Composable
@Preview
fun TextVerificationTruePreview(){
    TextCheck(true,"Is correct")
}

@Composable
@Preview
fun TextVerificationFalsePreview(){
    TextCheck(false,"Is incorrect")
}