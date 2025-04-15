package com.asier.arguments.ui.components.topbars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright0
import com.asier.arguments.ui.theme.TopBarBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopBar(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            content()
        },
        modifier = modifier,
        colors = TopAppBarColors(
            containerColor = TopBarBackground,
            titleContentColor = TextBright0,
            navigationIconContentColor = Color.White,
            actionIconContentColor =  Color.White,
            scrolledContainerColor = Color.Transparent
        ),
        actions = {})
}

@Composable
@Preview(showSystemUi = true)
fun BaseTopBarPreview(){
    BaseTopBar(content = { Text("Base") })
}