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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strikescore.R
import com.example.strikescore.model.Team
import com.example.strikescore.ui.components.TeamItem
import kotlinx.coroutines.launch

/**
 * Composable function for displaying the team overview screen.
 *
 * @param modifier Modifier for styling the composable.
 * @param teamOverviewViewModel ViewModel for handling data and business logic.
 */
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
//        when(workerState.workerInfo?.state){
//            null -> Text("state unknown")
//            else -> Text(workerState.workerInfo?.state!!.name)
//        }

        Box(modifier = modifier) {
            when (teamApiState) {
                is TeamApiState.Loading -> Text(stringResource(R.string.loading))
                is TeamApiState.Error -> Text(stringResource(R.string.couldn_t_load))
                is TeamApiState.Success -> TaskListComponent(teamOverviewState = teamOverviewState, teamListState = teamListState)
            }
        }
    }
}

/**
 * Composable function for displaying the list of teams.
 *
 * @param modifier Modifier for styling the composable.
 * @param teamOverviewState The state of the team overview screen.
 * @param teamListState The state of the team list.
 */
@Composable
fun TaskListComponent(modifier: Modifier = Modifier, teamOverviewState: TeamOverviewState, teamListState: TeamListState) {
    val lazyListState = rememberLazyListState()
    LazyColumn(state = lazyListState) {
        items(teamListState.teamList.size) {
            TeamItem(team = teamListState.teamList[it])
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