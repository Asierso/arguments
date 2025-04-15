package com.asier.arguments.ui.components.topbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asier.arguments.R
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright1
import com.asier.arguments.ui.theme.TopBarIcon

@Composable
fun TitleTopBar(
    title: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable() ((modifier : Modifier) -> Unit)? = null) {
    BaseTopBar(
       content = {
           Row(modifier = Modifier.fillMaxWidth()) {
               Row(
                   modifier = if(leadingIcon != null) Modifier.weight(.95f) else Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.Center,
                   verticalAlignment = Alignment.CenterVertically
                   ) {
                   if (leadingIcon != null) {
                       leadingIcon(Modifier.width(50.dp).height(50.dp).padding(end = 20.dp))
                   }
                   Text(
                       text = title,
                       textAlign = TextAlign.Center,
                       fontFamily = Montserrat,
                       fontWeight = FontWeight.SemiBold,
                       fontSize = 24.sp,
                       color = TextBright1,
                       modifier = Modifier
                   )
               }
               if(leadingIcon != null) {
                   Box(modifier = Modifier.weight(.05f)) {}
               }
           }
        },
        modifier = modifier)
}

@Composable
@Preview(showSystemUi = true)
fun TitleTopBarIconPreview(){
    TitleTopBar(title = "Topbar preview",
        leadingIcon = {
            Icon(painterResource(R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = it,
                tint = TopBarIcon
            )
        }
    )
}

@Composable
@Preview(showSystemUi = true)
fun TitleTopBarPreview(){
    TitleTopBar(title = "Topbar preview"
    )
}