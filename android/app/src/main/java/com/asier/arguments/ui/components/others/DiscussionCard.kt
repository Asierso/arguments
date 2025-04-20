package com.asier.arguments.ui.components.others

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asier.arguments.R
import com.asier.arguments.entities.DiscussionThread
import com.asier.arguments.entities.user.User
import com.asier.arguments.ui.theme.CardBackground
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.Primary
import com.asier.arguments.ui.theme.TextBright1
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun DiscussionCard(
    discussion: DiscussionThread,
    modifier: Modifier = Modifier,
    onUsernameClick: ((user: User) -> Unit)? = null
){
    Box(modifier = modifier
        .shadow(10.dp)) {
        //Background Progress bar
        Canvas(
            modifier = Modifier
                .clip(RoundedCornerShape(17.dp))
                .background(CardBackground)
                .matchParentSize()){
            val progressHeight = size.height

            //Calculate current progress
            val duration = Duration.between(discussion.createdAt,discussion.endAt).toMillis()
            val elapsedDuration = Duration.between(discussion.createdAt, LocalDateTime.now().atZone(ZoneOffset.UTC)).toMillis()

            val progressWidth = size.width - (size.width * (elapsedDuration.toFloat() / duration.toFloat()))
            drawRoundRect(
                color = Primary.copy(alpha = 0.3f),
                size = Size(progressWidth, size.height),
                topLeft = Offset(0f,0f),
                cornerRadius = CornerRadius.Zero
            )

        }

        Column(
            modifier = modifier
                .clip(RoundedCornerShape(17.dp))
                .fillMaxWidth()
                .padding(start = 10.dp, top = 15.dp, end = 10.dp, bottom = 5.dp)
        ) {
            Text(
                text = discussion.title,
                color = TextBright1,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                //Discussion author
                UserCard(
                    User().apply {
                        username = discussion.author
                    },
                    modifier = Modifier.padding(start = 5.dp, top = 10.dp, bottom = 10.dp)
                        .weight(.5f),
                    onClick = onUsernameClick
                )

                //Discussion properties
                Box(modifier = Modifier.weight(.5f), contentAlignment = Alignment.CenterEnd) {
                    Column(
                        modifier = Modifier.padding(end = 10.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        IconValue(value = "${discussion.users?.size}/${discussion.maxUsers}", {
                            Icon(
                                painter = painterResource(R.drawable.ic_person),
                                contentDescription = null, tint = TextBright1
                            )
                        })
                        IconValue(
                            value = "${
                                Duration.between(
                                    discussion.createdAt,
                                    discussion.endAt
                                ).toMinutes()
                            }\"", {
                                Icon(
                                    painter = painterResource(R.drawable.ic_clock),
                                    contentDescription = null, tint = TextBright1
                                )
                            })
                    }
                }
            }
        }

        //Expired indicator
        if (discussion.endAt!!.isBefore(LocalDateTime.now().atZone(ZoneOffset.UTC))) {
            Box(
                modifier = Modifier.clip(RoundedCornerShape(17.dp))
                    .alpha(.65f)
                    .background(Color.Black)
                    .matchParentSize()
                    .padding(start = 10.dp, top = 15.dp, end = 10.dp, bottom = 5.dp),
                contentAlignment = Alignment.TopCenter

            ) {
                Icon(
                    painterResource(R.drawable.ic_add),
                    contentDescription = "Expirado",
                    tint = TextBright1,
                    modifier = Modifier.rotate(45f).size(70.dp)
                )
                Text(
                    text = "Expirado",
                    color = TextBright1,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 60.dp)
                )
            }
        }
    }


}

@Composable
@Preview
fun DiscussionCardPreview(){
    DiscussionCard(
        DiscussionThread(
        title = "Lorem ipsum",
        author = "asierso",
        createdAt = LocalDateTime.now().atZone(ZoneOffset.UTC).minusMinutes(2),
            endAt = LocalDateTime.now().atZone(ZoneOffset.UTC).plusMinutes(1)))
}

@Composable
@Preview
fun DiscussionCardExpiredPreview(){
    DiscussionCard(
        DiscussionThread(
            title = "Lorem ipsum",
            author = "asierso",
            createdAt = LocalDateTime.now().atZone(ZoneOffset.UTC).minusMinutes(10),
            endAt = LocalDateTime.now().atZone(ZoneOffset.UTC).minusMinutes(5)))
}