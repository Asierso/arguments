package com.asier.arguments.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.asier.arguments.Screen
import com.asier.arguments.screens.ActivityProperties
import com.asier.arguments.ui.components.others.DiscussionCard
import com.asier.arguments.ui.components.others.UserAlt
import com.asier.arguments.ui.components.topbars.ProfileTopBar
import com.asier.arguments.ui.theme.TopBarBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(activityProperties: ActivityProperties? = null, homeScreenViewModel: HomeScreenViewModel){
    //List state used to make infinity scroll
    val listState = homeScreenViewModel.lazyList

    //Scope to make fetch
    val scope = rememberCoroutineScope()
    homeScreenViewModel.storage = activityProperties?.storage
    homeScreenViewModel.loadUsername()
    if(!homeScreenViewModel.firstLoaded){
        homeScreenViewModel.firstLoaded = true
        homeScreenViewModel.loadNextDiscussionsPage(scope)
    }

    //Change status bar color
    activityProperties?.window?.let {
        SideEffect {
            WindowCompat.getInsetsController(it, it.decorView)
                .isAppearanceLightStatusBars = true
            it.statusBarColor = TopBarBackground.toArgb()
        }
    }

    ProfileTopBar(title = "Discusiones",
        modifier = Modifier.fillMaxWidth(),
        profile = {UserAlt(name = homeScreenViewModel.username) {
            if(activityProperties != null){
                activityProperties.parameters["viewProfile"] = homeScreenViewModel.username
                homeScreenViewModel.loadSelfProfile(activityProperties)
            }
        }})

    val pullState = rememberPullToRefreshState()

    PullToRefreshBox(state = pullState, isRefreshing = homeScreenViewModel.pageRefreshing, onRefresh = {
        homeScreenViewModel.reloadDiscussionsPage(scope)
    }) {
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
            modifier = Modifier.fillMaxSize().padding(top = 100.dp)) {
            itemsIndexed(items = homeScreenViewModel.loadedDiscussions.toList()) { index, item ->
                DiscussionCard(
                    discussion = item,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                    onUsernameClick = {
                        if(activityProperties != null){
                            activityProperties.parameters["viewProfile"] = homeScreenViewModel.username
                            homeScreenViewModel.loadSelfProfile(activityProperties)
                        }
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
                   homeScreenViewModel.loadNextDiscussionsPage(scope)
                }
            }
    }


    Box(modifier = Modifier.fillMaxSize()){
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 30.dp, end = 20.dp),
            onClick = {
                //TODO New discussion
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