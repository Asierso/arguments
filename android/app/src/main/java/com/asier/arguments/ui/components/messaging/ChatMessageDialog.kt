package com.asier.arguments.ui.components.messaging

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asier.arguments.entities.Message
import com.asier.arguments.ui.components.others.UserAlt
import java.time.Instant

@Composable
fun ChatMessageDialog(
    message: Message,
    self: Boolean,
    modifier: Modifier = Modifier,
    userAlt: (@Composable () -> Unit)? = null
) {

    Row(modifier = modifier) {
        if(!self){
            userAlt?.invoke()
            Spacer(modifier = Modifier.padding(start = 5.dp))
        }
        else{
            Spacer(modifier = Modifier.weight(.1f))
        }
        MessageDialog(
            message = message,
            self = self,
            modifier = Modifier.weight(.8f)
        )
        if(!self){
            Spacer(modifier = Modifier.weight(.1f))
        }
    }
}

@Composable
@Preview
fun ChatMessageSelfDialogPreview(){
    ChatMessageDialog(message =
        Message(sender = "dummy", message = "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet", sendTime = Instant.now(), discussionId = ""),
        self = true,
        userAlt = {
            UserAlt(name = "dummy") { }
        }
    )
}

@Composable
@Preview
fun ChatMessageOtherDialogPreview(){
    ChatMessageDialog(message =
    Message(sender = "dummy", message = "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet", sendTime = Instant.now(), discussionId = ""),
        self = false,
        userAlt = {
            UserAlt(name = "dummy") { }
        }
    )
}