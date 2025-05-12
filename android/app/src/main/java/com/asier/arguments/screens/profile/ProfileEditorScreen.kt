package com.asier.arguments.screens.profile

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asier.arguments.R
import com.asier.arguments.entities.user.User
import com.asier.arguments.misc.PasswordPolicyCodes
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.alerts.WarningAlert
import com.asier.arguments.ui.components.backgrounds.ArgumentsPatternBackground
import com.asier.arguments.ui.components.buttons.PrimaryButton
import com.asier.arguments.ui.components.buttons.WarnedButton
import com.asier.arguments.ui.components.inputs.BaseTextInput
import com.asier.arguments.ui.components.inputs.IconTextInput
import com.asier.arguments.ui.components.others.TextCheck
import com.asier.arguments.ui.components.others.UserAlt
import com.asier.arguments.ui.components.others.UserCard
import com.asier.arguments.ui.components.others.hideKeyboard
import com.asier.arguments.ui.components.others.hideKeyboardOnClick
import com.asier.arguments.ui.components.topbars.ProfileTopBar
import com.asier.arguments.ui.theme.CardBackground
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright1

@SuppressLint("ContextCastToActivity")
@Composable
fun ProfileEditorScreen(profileEditorScreenViewModel: ProfileEditorScreenViewModel){
    //Scope to make fetch
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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

    ArgumentsPatternBackground(alpha = .05f, modifier = Modifier
        .fillMaxSize()
        .padding(5.dp))

    ProfileTopBar(title = profileEditorScreenViewModel.userData!!.username,
        modifier = Modifier.fillMaxWidth(),
        profile = {
            UserAlt(
                name = profileEditorScreenViewModel.userData!!.username) {}
        }
    )

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 100.dp, start = 10.dp, end = 10.dp)
    ) {
        UserExtendedCard(user = profileEditorScreenViewModel.userData!!, modifier = Modifier
            .shadow(shape = RoundedCornerShape(12.dp), elevation = 5.dp)
        )
        UserEditableDescriptionCard(profileEditorScreenViewModel, modifier = Modifier
            .padding(top = 10.dp)
            .shadow(shape = RoundedCornerShape(12.dp), elevation = 5.dp)
            .hideKeyboardOnClick()){
            profileEditorScreenViewModel.updateUserDescription(parameters,scope)
            hideKeyboard(context)
        }
        UserEditableActionsCard(profileEditorScreenViewModel, modifier = Modifier
            .padding(top = 10.dp)
            .shadow(shape = RoundedCornerShape(12.dp), elevation = 5.dp)
            .hideKeyboardOnClick()){
            profileEditorScreenViewModel.updateUserData(parameters,scope)
            hideKeyboard(context)
        }
    }

    //Warning area
    WarningAlert(
        title = stringResource(R.string.profile_account_delete_warning_title),
        subtitle = stringResource(R.string.profile_account_delete_warning_text),
        onConfirm = {
            profileEditorScreenViewModel.deleteUser(activityProperties,scope)
            profileEditorScreenViewModel.deleteAccountWarning = false
        },
        onDismiss = {
            profileEditorScreenViewModel.deleteAccountWarning = false
        },
        showAlert = profileEditorScreenViewModel.deleteAccountWarning
    )
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
            text = stringResource(R.string.profile_user_card_title),
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = TextBright1,
            fontSize = 24.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            //Discussion author
            UserCard(
                user = User().apply {
                    username = user.username
                    level = user.level
                    isActive = true
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
fun UserEditableDescriptionCard(profileEditorScreenViewModel: ProfileEditorScreenViewModel,modifier: Modifier = Modifier, updateAction: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(CardBackground)
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(R.string.profile_description_card_title),
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = TextBright1,
            fontSize = 24.sp
        )
        BaseTextInput(
            placeholder = stringResource(R.string.profile_description_card_title) + "...",
            text = profileEditorScreenViewModel.description,
            onValueChanged = {profileEditorScreenViewModel.description = it},
            readOnly = false,
            minLines = 5,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
        )
        //Update description button
        PrimaryButton(text = stringResource(R.string.profile_update_button),
            modifier = Modifier
                .fillMaxWidth()
                .hideKeyboardOnClick(),
            onClick = {
                updateAction()
            }
        )
    }
}

@Composable
fun UserEditableActionsCard(
    profileEditorScreenViewModel: ProfileEditorScreenViewModel,
    modifier: Modifier = Modifier,
    updateAction: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(CardBackground)
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(R.string.profile_actions_card_title),
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = TextBright1,
            fontSize = 24.sp,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()) {
            //Firstname input
            IconTextInput(
                modifier = Modifier.padding(top = 5.dp),
                onValueChanged = {profileEditorScreenViewModel.firstname = it},
                text = profileEditorScreenViewModel.firstname,
                leadingIcon = {
                    Icon(painterResource(R.drawable.ic_person), contentDescription = null)
                },
                placeholder = stringResource(R.string.register_firstname_field)
            )
            //Lastname input
            IconTextInput(
                modifier = Modifier.padding(top = 5.dp),
                onValueChanged = { profileEditorScreenViewModel.lastname = it},
                text = profileEditorScreenViewModel.lastname,
                leadingIcon = {
                    Icon(painterResource(R.drawable.ic_person), contentDescription = null)
                },
                placeholder = stringResource(R.string.register_lastname_field)
            )
            //Password input
            IconTextInput(
                modifier = Modifier.padding(top = 5.dp),
                onValueChanged = {
                    profileEditorScreenViewModel.password = it
                },
                text = profileEditorScreenViewModel.password,
                leadingIcon = {
                    Icon(painterResource(R.drawable.ic_key), contentDescription = null)
                },
                isError = profileEditorScreenViewModel.password.isNotBlank() && profileEditorScreenViewModel.checkPasswords() != PasswordPolicyCodes.STRONG,
                placeholder = stringResource(R.string.password_field),
                isPassword = true
            )
            //Password check component (only show it if username field was edited)
            if (profileEditorScreenViewModel.password.isNotBlank()) {
                TextCheck(
                    isCorrect = profileEditorScreenViewModel.checkPasswords() == PasswordPolicyCodes.STRONG,
                    reason =
                    when (profileEditorScreenViewModel.checkPasswords()) {
                        PasswordPolicyCodes.NOT_EQUALS -> stringResource(R.string.register_passwordpolicy_notequals)
                        PasswordPolicyCodes.WEAK -> stringResource(R.string.register_passwordpolicy_weak)
                        PasswordPolicyCodes.TOO_SHORT -> stringResource(R.string.register_passwordpolicy_tooshort)
                        PasswordPolicyCodes.STRONG -> stringResource(R.string.register_passwordpolicy_strong)
                        PasswordPolicyCodes.INVALID -> stringResource(R.string.register_passwordpolicy_invalid)
                    },
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
        }
        PrimaryButton(text = stringResource(R.string.profile_update_button),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .hideKeyboardOnClick(),
            onClick = {
                updateAction()
            })
        WarnedButton(text = stringResource(R.string.profile_delete_account_button),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .hideKeyboardOnClick(),
            onClick = {
                profileEditorScreenViewModel.deleteAccountWarning = true
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
    UserEditableActionsCard(ProfileEditorScreenViewModel()){}
}