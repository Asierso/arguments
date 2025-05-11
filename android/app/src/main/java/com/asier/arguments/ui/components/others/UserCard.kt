package com.asier.arguments.ui.components.others

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asier.arguments.R
import com.asier.arguments.entities.user.User
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.Tertiary
import com.asier.arguments.ui.theme.TextBright0
import org.apache.commons.lang3.StringUtils

@Composable
fun UserCard(
    modifier: Modifier = Modifier,
    user: User? = null,
    onClick: ((user: User) -> Unit)? = null
){
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(Tertiary)
            .padding(start = 10.dp, end = 15.dp, top = 7.dp, bottom = 7.dp)
            .clickable { if (user != null) onClick?.invoke(user) },
        verticalAlignment = Alignment.CenterVertically) {
        UserAlt(name = StringUtils.abbreviate(user?.username?: "??", 10), isOnline = user?.isActive) {
            if(user != null)
                onClick?.invoke(user)
        }
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = StringUtils.abbreviate(user?.username?: stringResource(R.string.user_card_noone),10),
                color = TextBright0,
                fontWeight = FontWeight.Medium,
                fontFamily = Montserrat,
                fontSize = 15.sp,
                modifier = Modifier.padding(start = 7.dp))
            Text(
                text = if(user == null) stringResource(R.string.user_card_nodata) else "Lvl ${user.level}",
                color = TextBright0,
                fontWeight = FontWeight.Medium,
                fontFamily = Montserrat,
                fontSize = 15.sp,
                modifier = Modifier.padding(start = 7.dp))
        }
    }
}

@Preview
@Composable
fun UserCardPreview(){
    UserCard(user = User().apply {
        firstname = "Antonio"
        lastname = "Garcia"
        username = "agarcia"
        level = 10
    })
}