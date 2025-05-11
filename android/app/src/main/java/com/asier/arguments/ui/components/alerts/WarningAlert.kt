package com.asier.arguments.ui.components.alerts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.asier.arguments.R
import com.asier.arguments.ui.components.buttons.PrimaryButton
import com.asier.arguments.ui.components.buttons.SecondaryButton
import com.asier.arguments.ui.theme.AlertBackground
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright1

@Composable
fun WarningAlert(
    title: String,
    subtitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    showAlert: Boolean = true){
    if(!showAlert)
        return

    AlertDialog(
        containerColor = AlertBackground,
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            PrimaryButton(text = stringResource(R.string.alert_acept), onClick = {
                onConfirm()
            }, modifier = Modifier.fillMaxWidth())
        },
        dismissButton = {
            SecondaryButton(text = stringResource(R.string.alert_cancel), onClick = {
                onDismiss()
            }, modifier = Modifier.fillMaxWidth())
        },
        title = {
            Text(
                text = title,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = TextBright1
            )
        },
        text = {
            Text(
                text = subtitle,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                color = TextBright1
            )
        },
        modifier = modifier
    )
}

@Composable
@Preview
fun WarningAlertPreview(){
    WarningAlert(title = "Title", subtitle = "Subtitle", onConfirm = {}, onDismiss = {})
}