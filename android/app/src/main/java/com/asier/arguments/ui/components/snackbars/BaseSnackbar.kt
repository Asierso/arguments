package com.asier.arguments.ui.components.snackbars

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import com.asier.arguments.R
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.PrimaryDark
import com.asier.arguments.ui.theme.TextBright0
import com.asier.arguments.ui.theme.TextBright2

@Composable
fun BaseSnackbar(message: String){
        Snackbar(
            modifier = Modifier.padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            contentColor = TextBright0,
            containerColor = PrimaryDark
        ){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = message,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = TextBright2
                )
            }

        }
}

@Preview
@Composable
fun BaseSnackbarPreciew(){
    BaseSnackbar("Base snackbar")
}