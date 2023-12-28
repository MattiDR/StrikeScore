package com.example.strikescore.ui.matchScreen

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
import com.example.strikescore.model.Match
import com.example.strikescore.model.Standings
import com.example.strikescore.model.Team
import com.example.strikescore.ui.components.MatchItem
import com.example.strikescore.ui.components.StandingsItem
import com.example.strikescore.ui.standingsScreen.StandingsApiState
import com.example.strikescore.ui.standingsScreen.StandingsListState
import com.example.strikescore.ui.standingsScreen.StandingsOverviewState
import com.example.strikescore.ui.standingsScreen.StandingsOverviewViewModel
import kotlinx.coroutines.launch
@Composable
fun MatchOverview(
    modifier: Modifier = Modifier,
    matchOverviewViewModel: MatchOverviewViewModel = viewModel(factory = MatchOverviewViewModel.Factory)
) {
    Log.i("vm inspection", "TeamOverview composition")
    val matchOverviewState by matchOverviewViewModel.uiState.collectAsState()
    val matchListState by matchOverviewViewModel.uiListState.collectAsState()

    // use the ApiState
    val matchApiState = matchOverviewViewModel.matchApiState

    //use the workerstate
    val workerState by matchOverviewViewModel.wifiWorkerState.collectAsState()
    Column {
//        when(workerState.workerInfo?.state){
//            null -> Text("state unknown")
//            else -> Text(workerState.workerInfo?.state!!.name)
//        }

        Box(modifier = modifier) {
            when (matchApiState) {
                is MatchApiState.Loading -> Text("Loading...")
                is MatchApiState.Error -> Text("Couldn't load...")
                is MatchApiState.Success -> MatchListComponent(matchOverviewState = matchOverviewState, matchListState = matchListState)
            }
        }
    }
}
@Composable
fun MatchListComponent(modifier: Modifier = Modifier, matchOverviewState: MatchOverviewState, matchListState: MatchListState) {
    val lazyListState = rememberLazyListState()
    LazyColumn(state = lazyListState) {
        items(matchListState.matchList.size) {
            MatchItem(match = matchListState.matchList[it])
        }
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(matchOverviewState.scrollActionIdx) {
        if (matchOverviewState.scrollActionIdx != 0) {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(matchOverviewState.scrollToItemIndex)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun matchListComponentPreview() {
    MatchListComponent(matchOverviewState = MatchOverviewState(), matchListState = MatchListState(listOf(
        Match(1, Team( "previewteam", "pre", "crest"), Team( "previewteam", "pre", "crest"), "", 1, "")
    ))
    )
}