package com.asier.arguments.ui.components.others

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextCorrect0
import com.asier.arguments.ui.theme.TextError0
import java.util.Objects
import kotlin.random.Random

@Composable
fun UserAlt(
    name: String,
    modifier: Modifier = Modifier,
    isOnline: Boolean? = null,
    useBorder: Boolean = true,
    actions: () -> Unit
) {

    Box {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(100.dp))
                .width(50.dp)
                .height(50.dp)
                .clickable { actions() }
                .background(getColorByName(name))
                .border(if(useBorder) 1.dp else 0.dp,if(useBorder) Color.Black else Color.Transparent, RoundedCornerShape(100.dp)),

            contentAlignment = Alignment.Center) {
            Text(
                text = if (name.length >= 2) name.substring(0, 2)
                    .toUpperCase(Locale.current) else "",
                fontSize = 20.sp,
                color = Color.White,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold
            )
        }
        //User online circle
        if(isOnline != null) {
            Box(
                modifier = Modifier
                    .matchParentSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .width(12.dp)
                        .height(12.dp)
                        .background(if (isOnline) TextCorrect0 else TextError0)
                        .border(2.dp,Color.Black, RoundedCornerShape(100.dp)),
                    contentAlignment = Alignment.BottomEnd
                ) {

                }
            }
        }
    }
}

fun getColorByName(name: String): Color {
    val hash = Objects.hash(name.toUpperCase(Locale.current))
    val rng = Random(hash)
    if(name == "??")
        return Color(0xFF232323)
    return Color(
        red = rng.nextInt(100, 255),
        green = rng.nextInt(100, 255),
        blue = rng.nextInt(100, 255)
    )
}

@Composable
@Preview
fun UserAltPreview0() {
    UserAlt("Marco") { }
}

@Composable
@Preview
fun UserAltPreview1() {
    UserAlt("Juan") { }
}

@Composable
@Preview
fun UserAltPreview2() {
    UserAlt("Luis") { }
}

@Composable
@Preview
fun UserAltPreview3() {
    UserAlt("Ruth") { }
}