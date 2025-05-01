package com.asier.arguments.ui.components.others

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asier.arguments.ui.theme.ListEvenItem
import com.asier.arguments.ui.theme.ListOddItem
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright1

@Composable
fun ListItem(
    position: Int,
    text: String,
    modifier: Modifier = Modifier
){
    Column (modifier = modifier.background(
        if(position % 2 == 0) ListEvenItem else ListOddItem
    )) {
        Text(
            text = text,
            color = TextBright1,
            fontFamily = Montserrat,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(
                top = 5.dp,
                bottom = 5.dp,
                start = 10.dp,
                end = 10.dp
            )
        )
    }
}

@Composable
@Preview
fun ListItemPreview(){
    ListItem(position = 0, text = "Texto", modifier = Modifier.fillMaxWidth())
}