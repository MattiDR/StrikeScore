package com.example.strikescore.ui.teamScreen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strikescore.model.Team
import com.example.strikescore.ui.components.TeamItem
import kotlinx.coroutines.launch

@Composable
fun TeamOverview(
    modifier: Modifier = Modifier,
    teamOverviewViewModel : TeamOverviewViewModel = viewModel(factory = TeamOverviewViewModel.Factory)
) {
    Log.i("vm inspection", "TeamOverview composition")
    val teamOverviewState by teamOverviewViewModel.uiState.collectAsState()
    val teamListState by teamOverviewViewModel.uiListState.collectAsState()

    // use the ApiState
    val teamApiState = teamOverviewViewModel.teamApiState

    //use the workerstate
    val workerState by teamOverviewViewModel.wifiWorkerState.collectAsState()
    Column {
        when(workerState.workerInfo?.state){
            null -> Text("state unknown")
            else -> Text(workerState.workerInfo?.state!!.name)
        }

        Box(modifier = modifier) {
            when (teamApiState) {
                is TeamApiState.Loading -> Text("Loading...")
                is TeamApiState.Error -> Text("Couldn't load...")
                is TeamApiState.Success -> TaskListComponent(teamOverviewState = teamOverviewState, teamListState = teamListState)
            }
        }
    }
}
@Composable
fun TaskListComponent(modifier: Modifier = Modifier, teamOverviewState: TeamOverviewState, teamListState: TeamListState) {
    val lazyListState = rememberLazyListState()
    LazyColumn(state = lazyListState) {
        items(teamListState.teamList.size) {
            TeamItem(name = teamListState.teamList[it].name)
        }
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(teamOverviewState.scrollActionIdx) {
        if (teamOverviewState.scrollActionIdx != 0) {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(teamOverviewState.scrollToItemIndex)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun TeamListComponentPreview() {
    TaskListComponent(teamOverviewState = TeamOverviewState(), teamListState = TeamListState(listOf(
        Team("previewteam", "pre", "crest", "address", "website", "clubColors", "venue")
    ))
    )
}