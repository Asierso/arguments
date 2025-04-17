package com.asier.arguments.ui.components.others

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asier.arguments.R
import com.asier.arguments.entities.DiscussionThread
import com.asier.arguments.entities.User
import com.asier.arguments.ui.theme.CardBackground
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright1
import java.time.Duration

@Composable
fun DiscussionCard(
    discussion: DiscussionThread,
    modifier: Modifier = Modifier,
    onUsernameClick: ((user: User) -> Unit)? = null
){
    Column(modifier = modifier
        .clip(RoundedCornerShape(17.dp))
        .background(CardBackground)
        .fillMaxWidth()
        .padding(start = 10.dp, top = 15.dp, end = 10.dp, bottom = 5.dp)){
        Text(
            text = discussion.title,
            color = TextBright1,
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 25.sp,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            //Discussion author
            UserCard(User().apply {
                username = discussion.author
            }, modifier = Modifier.padding(start = 5.dp, top = 10.dp, bottom = 10.dp).weight(.5f),
                onClick = onUsernameClick
            )

            //Discussion properties
            Box(modifier = Modifier.weight(.5f), contentAlignment = Alignment.CenterEnd) {
                Column(
                    modifier =Modifier.padding(end = 10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    IconValue(value = "${discussion.users?.size}/${discussion.maxUsers}", {
                        Icon(painter = painterResource(R.drawable.ic_person),
                            contentDescription = null, tint = TextBright1)
                    })
                    IconValue(value = "${Duration.between(discussion.createdAt,discussion.endAt).toMinutes()}\"" , {
                        Icon(painter = painterResource(R.drawable.ic_clock),
                            contentDescription = null, tint = TextBright1)
                    })
                }
            }

        }
    }
}

@Composable
@Preview
fun DiscussionCardPreview(){

}