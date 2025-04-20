package com.asier.arguments.screens.discussions

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asier.arguments.R
import com.asier.arguments.misc.PasswordPolicyCodes
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.buttons.PrimaryButton
import com.asier.arguments.ui.components.inputs.IconTextInput
import com.asier.arguments.ui.components.inputs.OnUpDown
import com.asier.arguments.ui.components.others.TextCheck
import com.asier.arguments.ui.components.others.UserAlt
import com.asier.arguments.ui.components.topbars.BaseTopBar
import com.asier.arguments.ui.components.topbars.ProfileActionTopBar
import com.asier.arguments.ui.components.topbars.TitleTopBar
import com.asier.arguments.ui.theme.CardBackground
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright1
import com.asier.arguments.ui.theme.TopBarBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("ContextCastToActivity")
@Composable
fun DiscussionThreadCreationScreen(dtcViewModel: DiscussionThreadCreationViewModel){
    //Activity parameters vm load
    val parameters: ActivityParameters = viewModel(LocalContext.current as ComponentActivity)
    val activityProperties: ActivityProperties = parameters.properties

    //Scope to make fetch
    val scope = rememberCoroutineScope()

    //Show overlay for few time when screen is changing
    LaunchedEffect(Unit) {
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                parameters.isLoading = true
                delay(500)
                parameters.isLoading = false
            }
        }
    }

    //Change status bar color
    activityProperties.window.let {
        SideEffect {
            WindowCompat.getInsetsController(it, it.decorView)
                .isAppearanceLightStatusBars = true
            it.statusBarColor = TopBarBackground.toArgb()
        }
    }

    TitleTopBar(
        title = "Nueva discusión",
        modifier = Modifier.fillMaxWidth())

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxSize().padding(top = 80.dp)) {

        //Rule modification
        DiscussionThink(modifier = Modifier.fillMaxWidth(), dtcViewModel = dtcViewModel)
        DiscussionRules(dtcViewModel = dtcViewModel, modifier = Modifier.padding(10.dp))

        Spacer(modifier = Modifier.height(60.dp))

        //Create discussion button
        PrimaryButton(text = "Crear", modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp), onClick = {
            dtcViewModel.createDiscussion(activityProperties,scope)
        })
    }
}

@Composable
fun DiscussionThink(dtcViewModel: DiscussionThreadCreationViewModel, modifier: Modifier = Modifier){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier) {
        Text(
            text = "¿En que estás pensando?",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = TextBright1,
            fontSize = 24.sp
        )
        IconTextInput(
            modifier = Modifier.padding(top = 10.dp),
            onValueChanged = {
                dtcViewModel.title = it
                dtcViewModel.titleModified = true
            },
            text = dtcViewModel.title,
            isError = dtcViewModel.titleModified && !dtcViewModel.titlePolicy(),
            leadingIcon = {
                Icon(painterResource(R.drawable.ic_focus), contentDescription = null)
            },
            placeholder = "Tema del debate"
        )
        if(dtcViewModel.titleModified) {
            TextCheck(
                isCorrect = dtcViewModel.titlePolicy(),
                reason = dtcViewModel.titleType(),
                modifier = Modifier
            )
        }
    }
}

@Composable
fun DiscussionRules(
    modifier: Modifier = Modifier,
    dtcViewModel: DiscussionThreadCreationViewModel){
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(CardBackground)
            .padding(top = 15.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
    ) {
        Text(
            text = "Reglas del debate",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = TextBright1,
            fontSize = 24.sp
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth()
        ) {
            //Max time
            IconTextInput(
                modifier = Modifier.padding(top = 5.dp),
                onValueChanged = { },
                text = if(dtcViewModel.maxTime < 0) "" else dtcViewModel.maxTime.toString(),
                suffix = " min",
                isError = dtcViewModel.maxTime != -1 && !dtcViewModel.timePolicy(),
                leadingIcon = {
                    Icon(painterResource(R.drawable.ic_time), contentDescription = null)
                },
                placeholder = "Tiempo maximo",
                upDown = object:OnUpDown{
                    override fun onUp(value: Int) {
                        dtcViewModel.maxTime = value
                    }

                    override fun onDown(value: Int) {
                        if(value <= 0)
                            dtcViewModel.maxTime = 0
                        else
                            dtcViewModel.maxTime = value
                    }
                }
            )
            //If time was modified, show discussion type by time
            if(dtcViewModel.maxTime != -1) {
                TextCheck(
                    isCorrect = dtcViewModel.timePolicy(),
                    reason = dtcViewModel.timeType(),
                    modifier = Modifier.padding(start = 10.dp).align(Alignment.Start)
                )
            }

            //Max users
            IconTextInput(
                modifier = Modifier.padding(top = 5.dp),
                onValueChanged = { },
                text = if(dtcViewModel.maxUsers < 0) "" else dtcViewModel.maxUsers.toString(),
                suffix = " p",
                leadingIcon = {
                    Icon(painterResource(R.drawable.ic_person), contentDescription = null)
                },
                isError = dtcViewModel.maxUsers != -1 && !dtcViewModel.userPolicy(),
                placeholder = "Usuarios máximos",
                upDown = object:OnUpDown{
                    override fun onUp(value: Int) {
                        dtcViewModel.maxUsers = value
                    }

                    override fun onDown(value: Int) {
                        if(value <= 0)
                            dtcViewModel.maxUsers = 0
                        else
                            dtcViewModel.maxUsers = value
                    }
                }
            )

            //If users was modified, show discussion type by user amount
            if(dtcViewModel.maxUsers != -1) {
                TextCheck(
                    isCorrect = dtcViewModel.userPolicy(),
                    reason = dtcViewModel.userType(),
                    modifier = Modifier.padding(start = 10.dp).align(Alignment.Start)
                )
            }
        }
    }
}

@Composable
@Preview
fun DiscussionRulesPreview(){
    DiscussionRules(dtcViewModel = DiscussionThreadCreationViewModel())
}