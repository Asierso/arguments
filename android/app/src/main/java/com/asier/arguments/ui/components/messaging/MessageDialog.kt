package com.asier.arguments.ui.components.messaging

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asier.arguments.R
import com.asier.arguments.entities.Message
import com.asier.arguments.ui.components.others.getColorByName
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.OtherMessageBox
import com.asier.arguments.ui.theme.SelfMessageBox
import com.asier.arguments.ui.theme.TextBright1
import org.apache.commons.lang3.StringUtils
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MessageDialog(
    message: Message,
    self: Boolean,
    modifier: Modifier = Modifier
){
    val normalRound = 15
    val sideRoundRect = 5
    val hour = message.sendTime.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"))
    Box(modifier = modifier
        .clip(
            RoundedCornerShape(
                topStart = if (self) normalRound.dp else sideRoundRect.dp,
                topEnd = if (self) sideRoundRect.dp else normalRound.dp,
                bottomEnd = normalRound.dp,
                bottomStart = normalRound.dp
            )
        )
        .background(if (self) SelfMessageBox else OtherMessageBox)
        .padding(10.dp)) {
        Column {
            if(!self){
                Text(
                    text = StringUtils.abbreviate(message.sender,15),
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Montserrat,
                    fontSize = 15.sp,
                    color = getColorByName(message.sender),
                    modifier = Modifier
                )
            }
            Text(
                text = message.message,
                textAlign = if(self) TextAlign.Justify else TextAlign.Justify,
                fontWeight = FontWeight.Medium,
                fontFamily = Montserrat,
                fontSize = 15.sp,
                color = TextBright1,
                modifier = Modifier.padding(top = 2.dp)
            )
            Text(
                text = "${getDayOf(message.sendTime, LocalContext.current)} ${stringResource(R.string.day_hour_prefix)} ${hour}",
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                fontFamily = Montserrat,
                fontSize = 10.sp,
                color = TextBright1,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .align(Alignment.End)
            )
        }
    }
}

private fun getDayOf(sendTime : Instant, context: Context) : String{
    val local = sendTime.atZone(ZoneId.systemDefault()).toLocalDate()
    if(local.equals(LocalDate.now())){
        return context.getString(R.string.day_today)
    }
    if(local.equals(LocalDate.now().minusDays(1))) {
        return context.getString(R.string.day_yesterday)
    }
    return local.format(DateTimeFormatter.ofPattern("dd/MM"))
}

@Preview
@Composable
fun MessageDialogPreview(){
    MessageDialog(
        Message(sender = "pepe", message = "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet", sendTime = Instant.now(), discussionId = ""),
        false)
}

@Preview
@Composable
fun MessageDialogSelfPreview(){
    MessageDialog(
        Message(sender = "dummy", message = "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet", sendTime = Instant.now(), discussionId = ""),
        true)
}