package com.asier.arguments.screens.profile

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asier.arguments.R
import com.asier.arguments.Screen
import com.asier.arguments.entities.User
import com.asier.arguments.misc.PasswordPolicyCodes
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.buttons.BaseButton
import com.asier.arguments.ui.components.buttons.PrimaryButton
import com.asier.arguments.ui.components.buttons.WarnedButton
import com.asier.arguments.ui.components.inputs.BaseTextInput
import com.asier.arguments.ui.components.inputs.IconTextInput
import com.asier.arguments.ui.components.others.IconValue
import com.asier.arguments.ui.components.others.UserAlt
import com.asier.arguments.ui.components.others.UserCard
import com.asier.arguments.ui.components.progressbars.XpProgressBar
import com.asier.arguments.ui.components.topbars.ProfileEditableTopBar
import com.asier.arguments.ui.components.topbars.ProfileTopBar
import com.asier.arguments.ui.theme.CardBackground
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright1
import org.apache.commons.lang3.StringUtils
import java.time.Duration

@SuppressLint("ContextCastToActivity")
@Composable
fun ProfileEditorScreen(profileEditorScreenViewModel: ProfileEditorScreenViewModel){
    //Scope to make fetch
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    //Activity parameters vm load
    val parameters: ActivityParameters = viewModel(LocalContext.current as ComponentActivity)
    val activityProperties: ActivityProperties = parameters.properties

    //Load storage in screen view model
    profileEditorScreenViewModel.storage = activityProperties.storage

    //Try to load user data
    profileEditorScreenViewModel.loadUserData(parameters, scope)

    //Deny to charge ui if userdata is null
    if(profileEditorScreenViewModel.userData == null){
        parameters.isLoading = true
        return
    }

    ProfileTopBar(title = profileEditorScreenViewModel.userData!!.username,
        modifier = Modifier.fillMaxWidth(),
        profile = {
            UserAlt(name = profileEditorScreenViewModel.userData!!.username) {}
        })

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 100.dp, start = 10.dp, end = 10.dp)
    ) {
        UserExtendedCard(user = profileEditorScreenViewModel.userData!!)
        UserEditableDescriptionCard(profileEditorScreenViewModel, modifier = Modifier.padding(top = 10.dp))
        UserEditableActionsCard(profileEditorScreenViewModel, modifier = Modifier.padding(top = 10.dp))
    }
}

@Composable
fun UserExtendedCard(modifier: Modifier = Modifier, user: User){
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(CardBackground)
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
    ){
        Text(
            text ="Tu tarjeta de usuario",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = TextBright1,
            fontSize = 28.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            //Discussion author
            UserCard(User().apply {
                username = user.username
            }, modifier = Modifier
                .padding(start = 2.dp, top = 10.dp, bottom = 10.dp)
                .weight(.60f),
                onClick = { }
            )

            Box(
                modifier = Modifier.weight(.40f),
                contentAlignment = Alignment.CenterEnd) {
                UserAlt(name = user.username, modifier = Modifier.size(70.dp)) { }
            }
        }
    }
}

@Composable
fun UserEditableDescriptionCard(profileEditorScreenViewModel: ProfileEditorScreenViewModel,modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(CardBackground)
            .padding(20.dp)
    ) {
        Text(
            text ="Descripción",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = TextBright1,
            fontSize = 28.sp
        )
        BaseTextInput(
            placeholder = "Descripcion...",
            text = profileEditorScreenViewModel.description,
            onValueChanged = {profileEditorScreenViewModel.description = it},
            readOnly = false,
            minLines = 5,
            modifier = Modifier.fillMaxWidth()
        )
        PrimaryButton(text = "Actualizar",
            modifier = modifier.fillMaxWidth(),
            onClick = {
                //TODO update description
            })
    }
}

@Composable
fun UserEditableActionsCard(profileEditorScreenViewModel: ProfileEditorScreenViewModel,modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(CardBackground)
            .padding(20.dp)
    ) {
        Text(
            text ="Cuenta y acciones",
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = TextBright1,
            fontSize = 28.sp,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 20.dp).fillMaxWidth()) {
            //Firstname input
            IconTextInput(
                modifier = Modifier.padding(top = 5.dp),
                onValueChanged = {},
                text = "",
                leadingIcon = {
                    Icon(painterResource(R.drawable.ic_person), contentDescription = null)
                },
                placeholder = stringResource(R.string.register_firstname_field)
            )
            //Lastname input
            IconTextInput(
                modifier = Modifier.padding(top = 5.dp),
                onValueChanged = { },
                text = "",
                leadingIcon = {
                    Icon(painterResource(R.drawable.ic_person), contentDescription = null)
                },
                placeholder = stringResource(R.string.register_lastname_field)
            )
            //Password input (pw1)
            IconTextInput(
                modifier = Modifier.padding(top = 5.dp),
                onValueChanged = {
                },
                text = "",
                leadingIcon = {
                    Icon(painterResource(R.drawable.ic_key), contentDescription = null)
                },
                placeholder = stringResource(R.string.password_field),
                isPassword = true
            )
        }
        PrimaryButton(text = "Actualizar",
            modifier = modifier.fillMaxWidth().padding(top = 10.dp),
            onClick = {
                //TODO update description
            })
        WarnedButton(text = "Borrar cuenta",
            modifier = modifier.fillMaxWidth().padding(top = 5.dp),
            onClick = {
                //TODO update description
            })
    }
}

@Composable
@Preview
fun UserExtendedCardPreview(){
    UserExtendedCard(modifier = Modifier, user = User(username = "dummy"))
}

@Composable
@Preview
fun UserEditableActionsCard(){
    UserEditableActionsCard(ProfileEditorScreenViewModel())
}