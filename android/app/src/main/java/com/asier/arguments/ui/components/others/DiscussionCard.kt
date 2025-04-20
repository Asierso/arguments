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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.res.stringResource
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
import com.asier.arguments.ui.theme.TextError0
import kotlinx.coroutines.delay
import org.apache.commons.lang3.StringUtils
import java.time.Duration
import java.time.Instant

@Composable
fun DiscussionCard(
    discussion: DiscussionThread,
    modifier: Modifier = Modifier,
    onUsernameClick: ((user: User) -> Unit)? = null
) {
    var redraw by remember { mutableStateOf(0) }

    //Launch redraw key
    LaunchedEffect(Unit) {
        while (discussion.endAt!!.isAfter(Instant.now())) {
            delay(1000)
            redraw++
        }
    }

    key(redraw) {
        val expired = discussion.endAt!!.isBefore(Instant.now())
        Box(
            modifier = modifier
                .shadow(10.dp)
        ) {
            //Background Progress bar
            Canvas(
                modifier = Modifier
                    .clip(RoundedCornerShape(17.dp))
                    .background(CardBackground)
                    .matchParentSize()
            ) {

                //Calculate current progress
                val duration = Duration.between(discussion.createdAt, discussion.endAt).toMillis()
                val elapsedDuration =
                    Duration.between(discussion.createdAt, Instant.now()).toMillis()

                val progressWidth =
                    size.width - (size.width * (elapsedDuration.toFloat() / duration.toFloat()))
                drawRoundRect(
                    color = if (1f - (elapsedDuration.toFloat() / duration.toFloat()) > .15f) Primary.copy(
                        alpha = 0.3f
                    ) else TextError0.copy(alpha = 0.3f),
                    size = Size(progressWidth, size.height),
                    topLeft = Offset(0f, 0f),
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
                    fontSize = 23.sp,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    //Discussion author
                    UserCard(
                        User().apply {
                            username = StringUtils.abbreviate(discussion.author, 10)
                        },
                        modifier = Modifier
                            .padding(start = 5.dp, top = 10.dp, bottom = 10.dp)
                            .weight(.6f),
                        onClick = {
                            if (!expired)
                                onUsernameClick?.invoke(it)
                        }
                    )

                    //Discussion properties
                    Box(modifier = Modifier.weight(.4f), contentAlignment = Alignment.CenterEnd) {
                        Column(
                            modifier = Modifier.padding(end = 10.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            IconValue(value = "${discussion.users.size}/${discussion.maxUsers}", {
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
            if (expired) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(17.dp))
                        .alpha(.65f)
                        .background(Color.Black)
                        .matchParentSize()
                        .padding(start = 10.dp, top = 15.dp, end = 10.dp, bottom = 5.dp),
                    contentAlignment = Alignment.Center

                ) {
                    Icon(
                        painterResource(R.drawable.ic_add),
                        contentDescription = stringResource(R.string.closed_discussion_text),
                        tint = TextBright1,
                        modifier = Modifier
                            .rotate(45f)
                            .padding(bottom = 30.dp)
                            .size(70.dp)
                    )
                    Text(
                        text = stringResource(R.string.closed_discussion_text),
                        color = TextBright1,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 25.sp,
                        modifier = Modifier.padding(start = 20.dp, end = 10.dp, top = 50.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun DiscussionCardPreview() {
    DiscussionCard(
        DiscussionThread(
            title = "Lorem ipsum",
            author = "asierso",
            createdAt = Instant.now().minusSeconds(5 * 60),
            endAt = Instant.now().plusSeconds(10 * 60)
        )
    )
}

@Composable
@Preview
fun DiscussionCard2Preview() {
    DiscussionCard(
        DiscussionThread(
            title = "Lorem ipsum",
            author = "asierso",
            createdAt = Instant.now().minusSeconds(9),
            endAt = Instant.now().plusSeconds(1)
        )
    )
}

@Composable
@Preview
fun DiscussionCardExpiredPreview() {
    DiscussionCard(
        DiscussionThread(
            title = "Lorem ipsum",
            author = "asierso",
            createdAt = Instant.now(),
            endAt = Instant.now()
        )
    )
}

@Composable
@Preview
fun DiscussionCardExpired2Preview() {
    DiscussionCard(
        DiscussionThread(
            title = "Lorem ipsum dolor sit amet Lorem ipsum",
            author = "asierso",
            createdAt = Instant.now(),
            endAt = Instant.now()
        )
    )
}