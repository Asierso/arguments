package com.asier.arguments.ui.components.others

import androidx.compose.foundation.background
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
fun SimpleListItem(
    position: Int,
    text: String,
    modifier: Modifier = Modifier,
    subtext: String = ""
) {
    Column(
        modifier = modifier.background(
            if (position % 2 == 0) ListEvenItem else ListOddItem
        )
    ) {
        Text(
            text = text,
            color = TextBright1,
            fontFamily = Montserrat,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(
                top = 10.dp,
                start = 20.dp,
                end = 20.dp,
                bottom = if(subtext.isNotBlank()) 0.dp else 10.dp
            )
        )
        if(subtext.isNotBlank()) {
            Text(
                text = subtext,
                color = TextBright1,
                fontFamily = Montserrat,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 10.dp
                )
            )
        }
    }
}

@Composable
@Preview
fun ListItemPreview() {
    SimpleListItem(
        position = 0,
        text = "Text",
        subtext = "Subtext",
        modifier = Modifier.fillMaxWidth()
    )
}