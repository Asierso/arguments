package com.asier.arguments.ui.components.topbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@Composable
fun ProfileActionTopBar(
    title: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
    profile: @Composable () -> Unit,
    actionIcon: @Composable ((modifier : Modifier) -> Unit) = { Icon(
        painter = painterResource(R.drawable.ic_edit),
        contentDescription = "edit",
        modifier = modifier)
    }) {
    BaseTopBar(
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(.15f)) {
                    profile()
                }
                Row(modifier = Modifier.weight(.70f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        color = TextBright1
                    )
                }
                IconButton(onClick = { onAction() },modifier = Modifier.weight(.15f)) {
                    actionIcon(Modifier.width(30.dp).height(30.dp))
                }

            }
        },
        modifier = modifier)
}

@Composable
@Preview(showSystemUi = true)
fun ProfileEditableTopBarPreview(){
    ProfileActionTopBar(title = "Profile name", onAction = {}, profile = {
        UserAlt("asier") { }
    })
}
