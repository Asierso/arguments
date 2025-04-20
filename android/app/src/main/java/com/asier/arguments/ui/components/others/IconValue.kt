package com.asier.arguments.ui.components.others

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asier.arguments.R
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright1

@Composable
fun IconValue(
    value : String,
    leadingIcon : @Composable () -> Unit,
    modifier: Modifier = Modifier,
    textSize: Int = 15){
    Row(verticalAlignment = Alignment.CenterVertically) {
        leadingIcon()
        Text(
            text = value,
            color = TextBright1,
            fontWeight = FontWeight.Medium,
            fontFamily = Montserrat,
            fontSize = textSize.sp,
            modifier = Modifier.padding(start = 5.dp))
    }
}

@Composable
@Preview
fun IconValuePreview(){
    IconValue(value = "0",{
        Icon(painter = painterResource(R.drawable.ic_person), contentDescription = null, tint = TextBright1)
    })
}