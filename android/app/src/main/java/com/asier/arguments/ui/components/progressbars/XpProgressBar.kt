package com.asier.arguments.ui.components.progressbars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright1

@Composable
fun XpProgressBar(currentXp : Int, maxXp : Int){
    Box(contentAlignment = Alignment.CenterEnd) {
        BaseProgressBar(
            progress = if(currentXp > maxXp) 1.0f  else currentXp.toFloat() /maxXp.toFloat(),
            modifier = Modifier.height(20.dp),
            radius = 5.dp)
        Text(
            text = "${currentXp}/${maxXp} XP",
            fontFamily = Montserrat,
            fontWeight = FontWeight.Medium,
            color = TextBright1,
            fontSize = 10.sp,
            modifier = Modifier.padding(end = 5.dp))
    }

}

@Composable
@Preview
fun XpProgressBarPreview(){
    XpProgressBar(currentXp = 60, maxXp = 120)
}