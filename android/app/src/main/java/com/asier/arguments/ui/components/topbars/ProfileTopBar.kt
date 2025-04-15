package com.asier.arguments.ui.components.topbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import com.asier.arguments.ui.components.others.UserAlt
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright1
import com.asier.arguments.ui.theme.TopBarIcon

@Composable
fun ProfileTopBar(
    title: String,
    modifier: Modifier = Modifier,
    profile: @Composable () -> Unit,
    leadingIcon: @Composable() ((modifier : Modifier) -> Unit)? = null) {
    BaseTopBar(
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(.15f)) {
                    profile()
                }
                Row(modifier = Modifier.weight(.75f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if(leadingIcon != null) Arrangement.Start else Arrangement.Center) {
                    if(leadingIcon != null){
                        leadingIcon(Modifier.width(50.dp).height(50.dp).padding(end = 20.dp))
                    }
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        color = TextBright1
                    )
                }
                if(leadingIcon==null){
                    Box(modifier=Modifier.weight(.15f))
                }

            }
        },
        modifier = modifier)
}

@Composable
@Preview(showSystemUi = true)
fun ProfileTopBarPreview(){
    ProfileTopBar(title = "Profile name", profile = {
        UserAlt("asier") { }
    })
}

@Composable
@Preview(showSystemUi = true)
fun ProfileTopBarIconPreview(){
    ProfileTopBar(title = "Profile name", profile = {
        UserAlt("asier") { }
    },leadingIcon = {
        Icon(
            painterResource(R.drawable.ic_logo),
            contentDescription = "Logo",
            modifier = it,
            tint = TopBarIcon
        )
    })
}