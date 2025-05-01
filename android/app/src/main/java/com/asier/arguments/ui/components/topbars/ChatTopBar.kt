package com.asier.arguments.ui.components.topbars

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.asier.arguments.R
import com.asier.arguments.entities.DiscussionThread
import com.asier.arguments.ui.theme.ChatProgressBackground
import com.asier.arguments.ui.theme.ChatUserIndicator
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.Primary
import com.asier.arguments.ui.theme.TextBright1
import com.asier.arguments.ui.theme.TopBarIcon
import kotlinx.coroutines.delay
import org.apache.commons.lang3.StringUtils
import java.time.Instant
import java.time.Duration

@Composable
fun ChatTopBar(
    discussion: DiscussionThread,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable() ((modifier: Modifier) -> Unit)? = null
) {
    var redraw by remember { mutableStateOf(0) }

    //Launch redraw key
    LaunchedEffect(Unit) {
        while (discussion.endAt == null || discussion.endAt!!.isAfter(Instant.now())) {
            delay(1000)
            redraw++
        }
    }

    //Parent container
    Box(modifier = modifier) {
        //Top bar with title and icon
        BaseTopBar(
            content = {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = if (leadingIcon != null) Modifier.weight(.95f) else Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (leadingIcon != null) {
                            leadingIcon(
                                Modifier
                                    .width(50.dp)
                                    .height(50.dp)
                                    .padding(end = 20.dp)
                            )
                        }
                        Text(
                            text = StringUtils.abbreviate(discussion.title,19),
                            textAlign = TextAlign.Center,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 23.sp,
                            color = TextBright1,
                            modifier = Modifier
                        )
                    }
                    if (leadingIcon != null) {
                        Box(modifier = Modifier.weight(.05f)) {}
                    }
                }

            },
        )

        //This part should be reloaded every second
        key(redraw) {
            //Background Progress bar
            Canvas(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStartPercent = 0,
                            topEndPercent = 0,
                            bottomStartPercent = 50,
                            bottomEndPercent = 50
                        )
                    )
                    .background(ChatProgressBackground)
                    .fillMaxWidth()
                    .height(7.dp)
                    .zIndex(1f)
                    .align(Alignment.BottomStart)
                    .clickable { }
            ) {
                //Calculate current progress
                val duration = if (discussion.createdAt == null) 0 else Duration.between(
                    discussion.createdAt,
                    discussion.endAt
                ).toMillis()
                val elapsedDuration = if (discussion.createdAt == null) 0 else Duration.between(
                    discussion.createdAt,
                    Instant.now()
                ).toMillis()

                val progressWidth =
                    size.width - (size.width * (elapsedDuration.toFloat() / duration.toFloat()))

                drawRoundRect(
                    color = Primary,
                    size = Size(progressWidth, size.height),
                    topLeft = Offset(0f, 0f),
                    cornerRadius = CornerRadius.Zero
                )
            }

            //Show card with the amount of users
            UserIndicator(
                currentUsers = discussion.users.size,
                maxUsers = discussion.maxUsers,
                modifier = Modifier.zIndex(1f).align(Alignment.BottomEnd).padding(bottom = 10.dp)
            )
        }
    }
}

@Composable
fun UserIndicator(
    currentUsers: Int,
    maxUsers: Int,
    modifier: Modifier = Modifier
){
    Box(modifier = modifier
        .clip(RoundedCornerShape(7.dp))
        .background(ChatUserIndicator)
        .padding(top = 3.dp, bottom = 3.dp, start = 6.dp, end = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_person),
                contentDescription = null,
                tint = TextBright1,
                modifier = Modifier.width(20.dp).height(20.dp))
            Text(
                text = "$currentUsers/$maxUsers",
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = TextBright1,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 7.dp)
            )
        }
    }
}

@Composable
@Preview
fun UserIndicatorPreview(){
    UserIndicator(
        currentUsers = 1,
        maxUsers = 5
    )
}

@Composable
@Preview(showSystemUi = true)
fun ChatTopBarIconPreview() {
    ChatTopBar(discussion = DiscussionThread(
        title = "Lorem ipsum dolor sit amets",
        createdAt = Instant.now().minusSeconds(120),
        endAt = Instant.now().plusSeconds(120)
    ),
        leadingIcon = {
            Icon(
                painterResource(R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = it,
                tint = TopBarIcon
            )
        }
    )
}

@Composable
@Preview(showSystemUi = true)
fun ChatTopBarPreview() {
    ChatTopBar(
        discussion = DiscussionThread(
            title = "Lorem ipsum dolor sit amets",
            createdAt = Instant.now().minusSeconds(120),
            endAt = Instant.now().plusSeconds(120)
        )
    )
}