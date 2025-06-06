package com.asier.arguments.screens.profile

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asier.arguments.R
import com.asier.arguments.Screen
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.entities.user.User
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.backgrounds.ArgumentsPatternBackground
import com.asier.arguments.ui.components.inputs.BaseTextInput
import com.asier.arguments.ui.components.others.SimpleListItem
import com.asier.arguments.ui.components.others.UserAlt
import com.asier.arguments.ui.components.progressbars.XpProgressBar
import com.asier.arguments.ui.components.topbars.ProfileActionTopBar
import com.asier.arguments.ui.components.topbars.ProfileTopBar
import com.asier.arguments.ui.theme.CardBackground
import com.asier.arguments.ui.theme.Montserrat
import com.asier.arguments.ui.theme.TextBright1
import com.asier.arguments.ui.theme.TopBarBackground
import org.apache.commons.lang3.StringUtils
import org.bson.types.ObjectId
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("ContextCastToActivity")
@Composable
fun ProfileScreen(
    profileScreenViewModel: ProfileScreenViewModel
) {
    //Scope to make fetch
    val scope = rememberCoroutineScope()

    //Activity parameters vm load
    val parameters: ActivityParameters = viewModel(LocalContext.current as ComponentActivity)
    val activityProperties: ActivityProperties = parameters.properties

    //Load storage in screen view model
    profileScreenViewModel.storage = activityProperties.storage

    //Try to load user data
    profileScreenViewModel.loadUserData(parameters, scope)

    //Change status bar color
    activityProperties.window.let {
        SideEffect {
            WindowCompat.getInsetsController(it, it.decorView)
                .isAppearanceLightStatusBars = true
            it.statusBarColor = TopBarBackground.toArgb()
        }
    }

    ArgumentsPatternBackground(alpha = .05f, modifier = Modifier.fillMaxSize().padding(5.dp))

    //Deny to charge ui if userdata is null
    if(profileScreenViewModel.userData == null){
        parameters.isLoading = true
        return
    }

    if(profileScreenViewModel.isSelf()){
        SelfProfileScreen(activityProperties,profileScreenViewModel)
    }else{
        ForeignProfileScreen(profileScreenViewModel)
    }


}

@Composable
fun SelfProfileScreen(activityProperties: ActivityProperties, profileScreenViewModel: ProfileScreenViewModel){
    ProfileActionTopBar(title = profileScreenViewModel.userData!!.username,
        modifier = Modifier.fillMaxWidth(),
        onAction = { activityProperties.navController.navigate(Screen.ProfileEdit.route)},
        profile = {
            UserAlt(
                name = profileScreenViewModel.userData!!.username,
                isOnline = if(profileScreenViewModel.isSelf()) true else profileScreenViewModel.userData!!.isActive) {}
        })

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 90.dp)
    ) {
        UserDetailCard(
            user = profileScreenViewModel.userData ?: User(),
            modifier = Modifier.padding(10.dp).shadow(shape = RoundedCornerShape(12.dp), elevation = 5.dp)
        )
        DiscussionsHistory(
            history = profileScreenViewModel.userData?.history
        )
    }
}

@Composable
fun ForeignProfileScreen(profileScreenViewModel: ProfileScreenViewModel){
    ProfileTopBar(title = profileScreenViewModel.userData!!.username,
        modifier = Modifier.fillMaxWidth(),
        profile = {
            UserAlt(
                name = profileScreenViewModel.userData!!.username,
                isOnline = profileScreenViewModel.userData!!.isActive) {
            }
        })

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 90.dp)
    ) {
        UserDetailCard(
            user = profileScreenViewModel.userData ?: User(),
            modifier = Modifier.padding(10.dp).shadow(shape = RoundedCornerShape(12.dp), elevation = 5.dp)
        )
        DiscussionsHistory(
            history = profileScreenViewModel.userData?.history
        )
    }
}

@Composable
fun UserDetailCard(user: User, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(CardBackground)
            .padding(10.dp)
    ) {
        //Name and level
        Column(modifier = Modifier.padding(start = 5.dp)) {
            Text(
                text = StringUtils.join(listOf(user.firstname, user.lastname), " "),
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = TextBright1,
                fontSize = 20.sp
            )
            Text(
                text = "Lvl ${user.level}",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                color = TextBright1,
                fontSize = 14.sp
            )
        }

        Box(modifier = Modifier.padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 5.dp)) {
            XpProgressBar(
                currentXp = user.xp,
                maxXp = 100,
                modifier = Modifier.fillMaxWidth()
            )
        }
        BaseTextInput(
            placeholder = stringResource(R.string.profile_description_card_title) + "...",
            text = user.description,
            onValueChanged = {},
            readOnly = true,
            minLines = 5,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DiscussionsHistory(
    modifier: Modifier = Modifier,
    history: HashMap<String,String>? = null) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(CardBackground)
    ) {
        Text(
            text = stringResource(R.string.profile_discussions_history_title),
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = TextBright1,
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp)
        )
        if(history == null){
            Text(
                text = "No hay debates aún",
                color = TextBright1.copy(alpha = .4f),
                fontFamily = Montserrat,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
        }else{
            val entries = history.entries.sortedBy { ObjectId(it.key).timestamp }.reversed().toList()
            LazyColumn {
                itemsIndexed(entries){ index, item ->
                    SimpleListItem(
                        position = index,
                        text = item.value,
                        subtext = DateTimeFormatter.ofPattern("dd/MM/yyyy '${stringResource(R.string.day_hour_prefix)}' HH:mm")
                            .withZone(ZoneId.systemDefault()).format(ObjectId(item.key).date.toInstant()),
                        modifier = Modifier.fillMaxWidth())
                }
            }
        }

    }
}

@Composable
@Preview
fun DiscussionsHistoryPreview() {
    DiscussionsHistory()
}

@Composable
@Preview
fun UserDetailCardPreview() {
    UserDetailCard(
        User(
            firstname = "Dummy",
            lastname = "Dummy",
            username = "dummy",
            level = 12,
            xp = 60,
            description = "Hello world, I'm dummy"
        )
    )
}
