package com.asier.arguments.ui.components.others

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asier.arguments.ui.theme.Background
import com.asier.arguments.ui.theme.CardBackground
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.Primary
import com.asier.arguments.ui.theme.TextBright0
import com.asier.arguments.ui.theme.TextBright2
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun VotingCard(
    scoreboard: HashMap<String,Int>,
    onCandidateClick: (candidate: Pair<String, Int>) -> Boolean,
    modifier: Modifier = Modifier,
    endVoting: Instant? = null
){
    var choosen by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Background)
            .border(
                width = 2.dp,
                color = CardBackground,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
            .heightIn(max = (60*4).dp)
    ) {
        Text(
            text = "Pulsa para votar ${
                if(endVoting == null || Instant.now().isAfter(endVoting)) "" else
                    Duration.between(Instant.now(), endVoting)
                        .seconds
                        .let { Instant.ofEpochSecond(it) }
                        .atZone(ZoneOffset.systemDefault())
                        .format(DateTimeFormatter.ofPattern("'('mm:ss')'"))}",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = TextBright2.copy(alpha = .5f)
        )

        val entries = scoreboard.entries.toList()
        val totalVotes = scoreboard.values.sumOf { it }

        LazyColumn {
            itemsIndexed(entries){ index, item ->
                Box(modifier = Modifier.padding(top=10.dp)) {
                    VotingOption(
                        candidate = item.toPair(),
                        totalVotes = totalVotes,
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        choosen = choosen == item.toPair().first,
                        onCandidateClick = {
                            if(onCandidateClick(it)){
                                choosen = it.first
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun VotingOption(
    candidate: Pair<String,Int>,
    totalVotes: Int,
    onCandidateClick: (candidate: Pair<String,Int>) -> Unit,
    modifier: Modifier = Modifier,
    choosen: Boolean = false
){
    Box(
        modifier = modifier
            .shadow(10.dp)
            .clickable { onCandidateClick(candidate) }
    ) {
        //Background Progress bar
        Canvas(
            modifier = Modifier
                .clip(RoundedCornerShape(7.dp))
                .background(CardBackground)
                .matchParentSize()
                .border(
                    width = if(choosen) 2.dp else 0.dp,
                    color = if(choosen) Primary else Color.Transparent,
                    shape = RoundedCornerShape(7.dp)
                )
        ) {

            val voteProgress = candidate.second.toFloat() / totalVotes.toFloat()

            val progressWidth =
                (size.width * voteProgress)
            drawRoundRect(
                color = getColorByName(candidate.first).copy(alpha = .8f),
                size = Size(progressWidth, size.height),
                topLeft = Offset(0f, 0f),
                cornerRadius = CornerRadius(10f,10f)
            )


        }

        Row(
            modifier = Modifier.matchParentSize(),
            verticalAlignment = Alignment.CenterVertically) {
            UserAlt(
                name = candidate.first,
                modifier = Modifier.padding(start = 8.dp)
                    .shadow(shape = RoundedCornerShape(100.dp), elevation = 5.dp),
                useBorder = false){}
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = candidate.first,
                    color = TextBright0,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Montserrat,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 7.dp))
                Text(
                    text = "Lvl 0",
                    color = TextBright0,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Montserrat,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 7.dp))
            }
        }
        Text(
            text = "${candidate.second}",
            color = TextBright0,
            fontWeight = FontWeight.SemiBold,
            fontSize = 25.sp,
            fontFamily = Montserrat,
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 15.dp)
        )
    }
}

@Composable
@Preview
fun VotingOptionPreview(){
    VotingOption(
        candidate = Pair("dummy",2),
        totalVotes = 3,
        modifier = Modifier.height(60.dp).fillMaxWidth(),
        onCandidateClick = {}
    )
}

@Composable
@Preview
fun VotingCardPreview(){
    VotingCard(
        scoreboard = hashMapOf(
            Pair("dummy1",1),
            Pair("dummy2",2),
            Pair("dummy3",2),
            Pair("dummy4",1),
            Pair("dummy5",1)),
        onCandidateClick = {
            true
        },
        endVoting = Instant.now().plusSeconds(123)
    )
}