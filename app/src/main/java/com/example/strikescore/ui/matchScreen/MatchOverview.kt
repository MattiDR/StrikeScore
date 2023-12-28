package com.example.strikescore.ui.matchScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strikescore.model.Match
import com.example.strikescore.model.Standings
import com.example.strikescore.model.Team
import com.example.strikescore.ui.components.DateItem
import com.example.strikescore.ui.components.MatchItem
import com.example.strikescore.ui.components.StandingsItem
import com.example.strikescore.ui.components.generateDateItems
import com.example.strikescore.ui.standingsScreen.StandingsApiState
import com.example.strikescore.ui.standingsScreen.StandingsListState
import com.example.strikescore.ui.standingsScreen.StandingsOverviewState
import com.example.strikescore.ui.standingsScreen.StandingsOverviewViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar

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

    //scroll date to today
    val datePickerLazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    //getting dates
    val currentDate = LocalDate.now()
    val dateItems = remember { generateDateItems(currentDate) }
    val selectedDateState = remember { mutableStateOf<LocalDate?>(currentDate) }

    LaunchedEffect(selectedDateState.value) {
        selectedDateState.value?.let { selectedDate ->
            // Calculate the index to scroll to
            val indexToScrollTo = dateItems.indexOfFirst { it.dayOfMonth == selectedDate.dayOfMonth }
            if (indexToScrollTo != -1) {
                coroutineScope.launch {
                    // Scroll to the index of the selected date
                    datePickerLazyListState.animateScrollToItem(indexToScrollTo)
                }
            }
        }
    }

    Column {
//        when(workerState.workerInfo?.state){
//            null -> Text("state unknown")
//            else -> Text(workerState.workerInfo?.state!!.name)
//        }

        DatePickerRow(
            dateItems = dateItems,
            selectedDate = selectedDateState.value,
            onDateSelected = { dateItem ->
                selectedDateState.value = LocalDate.of(currentDate.year, currentDate.month, dateItem.dayOfMonth)
                matchOverviewViewModel.selectDate(selectedDateState.value.toString())
            },
            lazyListState = datePickerLazyListState
        )


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

    if (matchListState.matchList.isNotEmpty()) {
        LazyColumn(state = lazyListState) {
            items(matchListState.matchList.size) {
                MatchItem(match = matchListState.matchList[it])
            }
        }
    } else {
        // Display "No matches" message when there are no matches
        Text("No matches for this day", modifier = Modifier.padding(16.dp))
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

@Composable
fun DatePickerRow(
    dateItems: List<DateItem>,
    selectedDate: LocalDate?,
    onDateSelected: (DateItem) -> Unit,
    lazyListState: LazyListState
) {
    LazyRow(modifier = Modifier.fillMaxWidth(), state = lazyListState) {
        items(dateItems) { dateItem ->
            val isSelected = selectedDate?.dayOfMonth == dateItem.dayOfMonth
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clickable { onDateSelected(dateItem) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .shadow(if (isSelected) 4.dp else 0.dp, RoundedCornerShape(10.dp))
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = dateItem.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = if (isSelected) 18.sp else 16.sp
                        )
                    )
                }
                Text(
                    text = dateItem.dayOfWeek,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                )
            }
        }
    }
}