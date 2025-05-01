package com.asier.arguments.ui.components.backgrounds

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.asier.arguments.R
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate

@Composable
fun ArgumentsPatternBackground(
    alpha: Float,
    modifier: Modifier = Modifier
){
    //Icon drawer constraints
    val iconSize = 13f
    val margin = 8f
    val iconBitmap = ImageBitmap.imageResource(R.drawable.ic_logo_minimal)

    //Draw background pattern
    Canvas(modifier = modifier) {

        //No. of icons to draw in canvas
        val columns = (size.width / iconSize).toInt()
        val rows = (size.height / iconSize).toInt()

        for (row in 0..rows) {
            for (col in 0..columns) {
                //Merge offset with animation constraint
                val x = (col * iconSize * margin ) - size.width
                val y = (row * iconSize * margin) -size.height

                //Draw arguments logo
                rotate(degrees = 16F, pivot = Offset(x, y)){
                    drawImage(
                        image = iconBitmap,
                        alpha = alpha,
                        topLeft = Offset(x, y)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun ArgumentsPatternBackgroundPreview(){
    ArgumentsPatternBackground(1f,Modifier.fillMaxSize())
}