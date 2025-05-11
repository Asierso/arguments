package com.asier.arguments.screens.rankings

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
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
import com.asier.arguments.ui.components.others.shineColorEffector
import com.asier.arguments.ui.components.topbars.TitleTopBar
import com.asier.arguments.ui.theme.Background
import com.asier.arguments.ui.theme.CardBackground
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.Primary
import com.asier.arguments.ui.theme.PrimaryDark
import com.asier.arguments.ui.theme.TextBright1
import com.asier.arguments.ui.theme.TextBright2
import com.asier.arguments.ui.theme.TopBarBackground
import com.asier.arguments.ui.theme.TopBarIcon
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

    ArgumentsPatternBackground(
        alpha = .05f, modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    )


        rankingsScreenViewModel.loadUserXp(scope)
        rankingsScreenViewModel.loadMessages(scope)
        rankingsScreenViewModel.loadRanking(scope)


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

    //Change between ranking views
    when (rankingsScreenViewModel.screen) {
        RankingsScreenViewModel.RankingScreen.OVERVIEW -> {
            RankedOverviewScreen(rankingsScreenViewModel, onReturnClick = {rankingsScreenViewModel.goBackButton(activityProperties)})
        }

        RankingsScreenViewModel.RankingScreen.LIST -> {
            RankingListScreen(rankingsScreenViewModel,activityProperties.storage.load("user")!!)
        }
    }

}

@Composable
fun RankedOverviewScreen(rankingsScreenViewModel: RankingsScreenViewModel, onReturnClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(top = 100.dp)
            .fillMaxHeight()
    ) {
        RankedPositionCard(
            position = rankingsScreenViewModel.selfPosition,
            gainedXp = rankingsScreenViewModel.xpEarn,
            totalXp = rankingsScreenViewModel.totalXp,
            onRankingClick = {
                rankingsScreenViewModel.screen = RankingsScreenViewModel.RankingScreen.LIST
            },
            modifier = Modifier.padding(10.dp)
        )

        ArgumentsList(
            modifier = Modifier.padding(top = 10.dp, start = 1.dp, end = 1.dp),
            userMessages = rankingsScreenViewModel.messages,
            onReturnClick = {
                onReturnClick()
            }
        )
    }
}

@Composable
fun RankingListScreen(rankingsScreenViewModel: RankingsScreenViewModel, username: String) {
    Column(modifier = Modifier
        .padding(top = 110.dp)
        .fillMaxHeight()) {
        Text(
            text = "Ranking",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = TextBright1,
            fontSize = 28.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(rankingsScreenViewModel.sortedRanking.entries.toList()) { index, item ->
                //Print every rank position sorted (show name, votes and xp earned)
                RankedPositionListElement(
                    voteData = item.toPair(),
                    isSelf = item.key == username,
                    xp = rankingsScreenViewModel.xpRanking[item.key]?: 0,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
        //Change view to overview
        PrimaryButton(
            text = "Ver resumen",
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            padding = PaddingValues(10.dp, 15.dp),
            onClick = {
                rankingsScreenViewModel.screen = RankingsScreenViewModel.RankingScreen.OVERVIEW
            }
        )
    }
}

@Composable
fun RankedPositionListElement(
    voteData: Pair<String, Int>,
    xp: Int,
    modifier: Modifier = Modifier,
    isSelf: Boolean = false
) {
    Box(modifier = modifier) {
        Row(
            modifier =  Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if(isSelf) Primary else CardBackground)
                .padding(10.dp)
        ) {
            UserAlt(
                name = voteData.first,
                useBorder = false,
                modifier = Modifier
                    .height(45.dp)
                    .width(45.dp)
            ) { }
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = voteData.first,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = TextBright1,
                    fontSize = 20.sp
                )
                Text(
                    text = "+${xp} XP",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Medium,
                    color = TextBright1,
                    fontSize = 15.sp
                )
            }
        }
        Text(
            text = "${voteData.second}",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = TextBright1,
            fontSize = 28.sp,
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 15.dp)
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
) {
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
                xp = totalXp.toFloat(),
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
            padding = PaddingValues(10.dp, 15.dp),
            onClick = { onRankingClick() }
        )
    }
}

@Composable
fun ArgumentsList(
    userMessages: MutableList<Message>?,
    onReturnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                .padding(2.dp)
                //.heightIn(max = (60 * 4).dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            //If messages are loading or there's no message
            if (userMessages.isNullOrEmpty()) {
                item {
                    Text(
                        text = "Sin mensajes",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Medium,
                        color = TextBright2,
                        fontSize = 18.sp,
                    )
                }
                return@LazyColumn
            }

            //If there's messages to show, load it
            itemsIndexed(userMessages) { index, item ->
                Row(modifier = Modifier.padding(8.dp)) {
                    Column(modifier = Modifier.weight(.5f)) {
                        Icon(
                            painter = painterResource(R.drawable.ic_ia),
                            contentDescription = "ia",
                            tint = shineColorEffector().value,
                            modifier = Modifier
                                .width(15.dp)
                                .height(15.dp)
                        )
                        Text(
                            text = if (item.feedback.toLowerCase(Locale.current)
                                    .replace("\"", "") == "unknown"
                            ) "..." else item.feedback,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Normal,
                            color = TextBright1,
                            textAlign = TextAlign.Justify,
                            fontSize = 13.sp
                        )
                    }
                    MessageDialog(
                        message = item,
                        self = true,
                        modifier = Modifier.weight(.5f)
                    )
                }
            }
        }
        PrimaryButton(
            text = "Volver",
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            padding = PaddingValues(10.dp, 15.dp),
            onClick = {
                onReturnClick()
            }
        )
    }
}

@Composable
fun CanvasXpBar(
    xp: Float,
    modifier: Modifier = Modifier
) {
    //Background Progress bar
    Canvas(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    9.dp
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
fun RankedPositionPreviewCard() {
    RankedPositionCard(
        position = 1,
        gainedXp = 12,
        totalXp = 50,
        onRankingClick = {})
}

@Composable
@Preview
fun ArgumentsListPreview() {
    ArgumentsList(
        onReturnClick = {},
        userMessages = mutableListOf(
            Message(
                message = "Hello",
                sendTime = Instant.now(),
                discussionId = "0",
                sender = "dummy",
                feedback = ""
            ),
        )
    )
}

@Composable
@Preview
fun RankedPositionListElementPreview(
    modifier: Modifier = Modifier
) {
    RankedPositionListElement(
        modifier = Modifier.fillMaxWidth(),
        xp = 10,
        voteData = Pair("dummy",1)
    )
}