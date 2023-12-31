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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strikescore.R
import com.example.strikescore.ui.components.StandingsItem
import kotlinx.coroutines.launch

/**
 * Composable function for displaying the standings overview screen.
 *
 * @param modifier Modifier for styling the composable.
 * @param standingsOverviewViewModel ViewModel for handling data and business logic.
 */
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

    Column {
        Box(modifier = modifier) {
            when (standingsApiState) {
                is StandingsApiState.Loading -> Text(stringResource(R.string.loading))
                is StandingsApiState.Error -> Text(stringResource(R.string.couldn_t_load))
                is StandingsApiState.Success -> StandingsListComponent(standingsOverviewState = standingsOverviewState, standingsListState = standingsListState)
            }
        }
    }
}
/**
 * Composable function for displaying the list of standings.
 *
 * @param standingsOverviewState State representing the overview of standings.
 * @param standingsListState State representing the list of standings.
 */
@Composable
fun StandingsListComponent(standingsOverviewState: StandingsOverviewState, standingsListState: StandingsListState) {
    val lazyListState = rememberLazyListState()
    LazyColumn(state = lazyListState) {
        items(standingsListState.standingsList.size) {
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