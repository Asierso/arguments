package com.asier.arguments.screens.home

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asier.arguments.R
import com.asier.arguments.Screen
import com.asier.arguments.screens.ActivityParameters
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.others.DiscussionCard
import com.asier.arguments.ui.components.others.UserAlt
import com.asier.arguments.ui.components.topbars.ProfileActionTopBar
import com.asier.arguments.ui.components.topbars.ProfileTopBar
import com.asier.arguments.ui.theme.TopBarBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen( homeScreenViewModel: HomeScreenViewModel){
    //List state used to make infinity scroll
    val listState = homeScreenViewModel.lazyList

    //Activity parameters vm load
    val parameters: ActivityParameters = viewModel(LocalContext.current as ComponentActivity)
    val activityProperties: ActivityProperties = parameters.properties

    //Scope to make fetch
    val scope = rememberCoroutineScope()
    homeScreenViewModel.storage = activityProperties.storage

    homeScreenViewModel.loadUsername()

    //Show overlay for few time when screen is changing
    LaunchedEffect(Unit) {
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                parameters.isLoading = true
                delay(1500)
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

    ProfileActionTopBar(title = "Discusiones",
        modifier = Modifier.fillMaxWidth(),
        profile = {UserAlt(name = homeScreenViewModel.username) {
                parameters.viewProfile = homeScreenViewModel.username
                homeScreenViewModel.loadProfile(activityProperties)
        }},
        onAction = {
            homeScreenViewModel.logout(activityProperties,scope)
        }){
        Icon(painterResource(R.drawable.ic_logout), contentDescription = "logout")
    }

    val pullState = rememberPullToRefreshState()

    PullToRefreshBox(state = pullState, isRefreshing = homeScreenViewModel.pageRefreshing, onRefresh = {
        homeScreenViewModel.reloadDiscussionsPage(parameters,scope)
        scope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                parameters.isLoading = true
                delay(1500)
                parameters.isLoading = false
            }
        }
    }) {
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
            modifier = Modifier.fillMaxSize().padding(top = 100.dp)) {
            itemsIndexed(items = homeScreenViewModel.loadedDiscussions.toList()) { index, item ->
                DiscussionCard(
                    discussion = item.discussionThread,
                    userData = item.user.also {
                        it!!.isActive = if(it.username == homeScreenViewModel.username) true else it.isActive
                    },
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                    onUsernameClick = {
                        parameters.viewProfile = it.username
                        homeScreenViewModel.loadProfile(activityProperties)
                    })
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val visibleItems = layoutInfo.visibleItemsInfo
                val totalItems = layoutInfo.totalItemsCount
                val lastVisibleIndex = visibleItems.lastOrNull()?.index ?: 0

                if (lastVisibleIndex >= totalItems - 1) {
                   homeScreenViewModel.loadNextDiscussionsPage(parameters, scope)
                }
            }
    }

    Box(modifier = Modifier.fillMaxSize()){
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 30.dp, end = 20.dp),
            onClick = {
                activityProperties.navController.navigate(Screen.DiscussionCreate.route)
            }) {
            Text(
                text = "+",
                fontSize = 30.sp)
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HomeScreenPreview(){
    HomeScreen(homeScreenViewModel = HomeScreenViewModel())
}