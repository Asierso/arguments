package com.asier.arguments.screens.rankings

import android.annotation.SuppressLint
import android.icu.text.CaseMap.Title
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asier.arguments.R
import com.asier.arguments.entities.Message
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.backgrounds.ArgumentsPatternBackground
import com.asier.arguments.ui.components.buttons.PrimaryButton
import com.asier.arguments.ui.components.messaging.MessageDialog
import com.asier.arguments.ui.components.others.UserAlt
import com.asier.arguments.ui.components.progressbars.BaseProgressBar
import com.asier.arguments.ui.components.progressbars.XpProgressBar
import com.asier.arguments.ui.components.topbars.ProfileActionTopBar
import com.asier.arguments.ui.components.topbars.TitleTopBar
import com.asier.arguments.ui.theme.Background
import com.asier.arguments.ui.theme.CardBackground
import com.asier.arguments.ui.theme.ChatProgressBackground
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.Primary
import com.asier.arguments.ui.theme.PrimaryDark
import com.asier.arguments.ui.theme.TextBright1
import com.asier.arguments.ui.theme.TopBarBackground
import com.asier.arguments.ui.theme.TopBarIcon
import java.time.Duration
import java.time.Instant

@SuppressLint("ContextCastToActivity")
@Composable
fun RankingsScreen(rankingsScreenViewModel: RankingsScreenViewModel) {
    //Activity parameters vm load
    val parameters: ActivityParameters = viewModel(LocalContext.current as ComponentActivity)
    val activityProperties: ActivityProperties = parameters.properties

    val scope = rememberCoroutineScope()
    rankingsScreenViewModel.storage = activityProperties.storage

    //Change status bar color
    activityProperties.window.let {
        SideEffect {
            WindowCompat.getInsetsController(it, it.decorView)
                .isAppearanceLightStatusBars = true
            it.statusBarColor = TopBarBackground.toArgb()
        }
    }

    ArgumentsPatternBackground(alpha = .05f, modifier = Modifier
        .fillMaxSize()
        .padding(5.dp))

    TitleTopBar(title = "Discusiones",
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                painterResource(R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = it,
                tint = TopBarIcon
            )
        })

    Column(
        modifier = Modifier.padding(top = 100.dp)
    ) {
        RankedPositionCard(
            position = 1,
            gainedXp = 12,
            totalXp = 50,
            onRankingClick = {},
            modifier = Modifier.padding(10.dp)
        )

        ArgumentsList(
            modifier = Modifier.padding(top = 10.dp),
            userMessages = listOf(
                Message(message = "Hello", sendTime = Instant.now(), discussionId = "0", sender = "dummy"),
            )
        )
    }
}

@Composable
fun RankedPositionCard(
    position: Int,
    gainedXp: Int,
    totalXp: Int,
    onRankingClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(CardBackground)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Fin del debate!",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = TextBright1,
            fontSize = 22.sp
        )
        Text(
            text = "Has quedado ${position}º",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = TextBright1,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column {
            CanvasXpBar(
                xp = 1f,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "+${gainedXp}XP",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Medium,
                    color = TextBright1,
                    fontSize = 12.sp
                )
                Text(
                    text = "${totalXp}/100 XP",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Medium,
                    color = TextBright1,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        PrimaryButton(
            text = "Ver ranking",
            modifier = Modifier.fillMaxWidth(),
            padding = PaddingValues(10.dp,15.dp),
            onClick = { onRankingClick() }
        )
    }
}

@Composable
fun ArgumentsList(
    userMessages: List<Message>,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Tus argumentos",
            fontFamily = Montserrat,
            fontWeight = FontWeight.Medium,
            color = TextBright1,
            fontSize = 28.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Background)
                .border(
                    width = 2.dp,
                    color = CardBackground,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp)
                .heightIn(max = (60 * 4).dp)
                .fillMaxWidth()
        ) {
            itemsIndexed(userMessages) { index, item ->
                Row(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Feedback...",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Normal,
                        color = TextBright1,
                        fontSize = 18.sp,
                        modifier = Modifier.weight(.5f)
                    )
                    MessageDialog(
                        message = item,
                        self = true,
                        modifier = Modifier.weight(.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun CanvasXpBar(
    xp: Float,
    modifier: Modifier = Modifier
){
    //Background Progress bar
    Canvas(
        modifier = modifier
            .clip(
                RoundedCornerShape(9.dp
                )
            )
            .background(PrimaryDark)
            .height(30.dp)
            .zIndex(1f)
    ) {
        //Calculate current xp
        val progressWidth =
            size.width * (xp / 100)

        drawRoundRect(
            color = Primary,
            size = Size(progressWidth, size.height),
            topLeft = Offset(0f, 0f),
            cornerRadius = CornerRadius.Zero
        )
    }
}

@Composable
@Preview
fun RankedPositionPreviewCard(){
RankedPositionCard(
    position = 1,
    gainedXp = 12,
    totalXp = 50,
    onRankingClick = {})
}

@Composable
@Preview
fun ArgumentsListPreview(){
    ArgumentsList(
        userMessages = listOf(
            Message(message = "Hello", sendTime = Instant.now(), discussionId = "0", sender = "dummy"),
            )
    )
}