package com.example.strikescore.ui.standingsScreen

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
import com.example.strikescore.model.Standings
import com.example.strikescore.model.Team
import com.example.strikescore.ui.components.StandingsItem
import com.example.strikescore.ui.components.TeamItem
import com.example.strikescore.ui.teamScreen.TeamApiState
import com.example.strikescore.ui.teamScreen.TeamListState
import com.example.strikescore.ui.teamScreen.TeamOverviewState
import com.example.strikescore.ui.teamScreen.TeamOverviewViewModel
import kotlinx.coroutines.launch


@Composable
fun StandingsOverview(
    modifier: Modifier = Modifier,
    standingsOverviewViewModel : StandingsOverviewViewModel = viewModel(factory = StandingsOverviewViewModel.Factory)
) {
    Log.i("vm inspection", "TeamOverview composition")
    val standingsOverviewState by standingsOverviewViewModel.uiState.collectAsState()
    val standingsListState by standingsOverviewViewModel.uiListState.collectAsState()

    // use the ApiState
    val standingsApiState = standingsOverviewViewModel.standingsApiState

    //use the workerstate
    val workerState by standingsOverviewViewModel.wifiWorkerState.collectAsState()
    Column {
//        when(workerState.workerInfo?.state){
//            null -> Text("state unknown")
//            else -> Text(workerState.workerInfo?.state!!.name)
//        }

        Box(modifier = modifier) {
            when (standingsApiState) {
                is StandingsApiState.Loading -> Text("Loading...")
                is StandingsApiState.Error -> Text("Couldn't load...")
                is StandingsApiState.Success -> TaskListComponent(standingsOverviewState = standingsOverviewState, standingsListState = standingsListState)
            }
        }
    }
}
@Composable
fun TaskListComponent(modifier: Modifier = Modifier, standingsOverviewState: StandingsOverviewState, standingsListState: StandingsListState) {
    val lazyListState = rememberLazyListState()
    LazyColumn(state = lazyListState) {
        items(standingsListState.standingsList.size) {
//            TeamItem(name = standingsListState.teamList[it].name, crest = teamListState.teamList[it].crest)
            StandingsItem(standing = standingsListState.standingsList[it])
        }
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(standingsOverviewState.scrollActionIdx) {
        if (standingsOverviewState.scrollActionIdx != 0) {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(standingsOverviewState.scrollToItemIndex)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun standingsListComponentPreview() {
    TaskListComponent(standingsOverviewState = StandingsOverviewState(), standingsListState = StandingsListState(listOf(
        Standings(1, Team( "previewteam", "pre", "crest"), 1, 1, 1, 1, 1, 1, 1, 1)
    ))
    )
}