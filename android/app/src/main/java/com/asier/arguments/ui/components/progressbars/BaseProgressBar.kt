package com.asier.arguments.ui.components.progressbars

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asier.arguments.ui.theme.Primary
import com.asier.arguments.ui.theme.PrimaryDark

@Composable
fun BaseProgressBar(modifier : Modifier = Modifier, progress : Float, radius : Dp = 10.dp){
    LinearProgressIndicator(
        progress = { progress },
        trackColor = PrimaryDark,
        strokeCap = StrokeCap.Round,
        color = Primary,
        modifier = modifier.clip(RoundedCornerShape(radius)).background(PrimaryDark),
        drawStopIndicator = {}
    )
}

@Preview
@Composable
fun BaseProgressBarPreview(){
    BaseProgressBar(progress = .5f)
}
