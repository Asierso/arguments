package com.asier.arguments.ui.components.topbars

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.asier.arguments.ui.theme.Montserrat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopBar(
    title: String,
    modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
        Text(text = title,
            textAlign = TextAlign.Center,
            fontFamily = Montserrat)
        },
        modifier = modifier,
        actions = {})
}

@Composable
@Preview(showSystemUi = true)
fun BaseTopBarPreview(){
    BaseTopBar(title = "Topbar prev")
}