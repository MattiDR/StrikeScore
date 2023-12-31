package com.example.strikescore.ui.matchScreen

import android.util.Log
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strikescore.R
import com.example.strikescore.ui.components.DateItem
import com.example.strikescore.ui.components.MatchItem
import com.example.strikescore.ui.components.generateDateItems
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Composable function representing the match overview screen.
 *
 * @param modifier Modifier for styling the [MatchOverview] composable.
 * @param matchOverviewViewModel [MatchOverviewViewModel] used to manage the UI state for the match overview.
 */
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
                is MatchApiState.Loading -> Text(stringResource(R.string.loading))
                is MatchApiState.Error -> Text(stringResource(R.string.couldn_t_load))
                is MatchApiState.Success -> MatchListComponent(matchOverviewState = matchOverviewState, matchListState = matchListState)
            }
        }
    }
}
/**
 * Composable function representing the list of matches in the match overview screen.
 *
 * @param matchOverviewState [MatchOverviewState] containing information about the match overview screen state.
 * @param matchListState [MatchListState] containing the list of matches and related state.
 */
@Composable
fun MatchListComponent(matchOverviewState: MatchOverviewState, matchListState: MatchListState) {
    val lazyListState = rememberLazyListState()

    if (matchListState.matchList.isNotEmpty()) {
        LazyColumn(state = lazyListState) {
            items(matchListState.matchList.size) {
                MatchItem(match = matchListState.matchList[it])
            }
        }
    } else {
        // Display "No matches" message when there are no matches
        Text(text = stringResource(R.string.waiting_or_no_matches))

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
/**
 * Composable function representing the date picker row in the match overview screen.
 *
 * @param dateItems List of [DateItem] representing the dates to be displayed in the row.
 * @param selectedDate Selected date to highlight in the row.
 * @param onDateSelected Callback when a date is selected.
 * @param lazyListState [LazyListState] used for controlling the scroll state of the date picker row.
 */
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
                    .padding(horizontal = dimensionResource(R.dimen.padding_small), vertical = dimensionResource(R.dimen.padding_xsmall))
                    .clickable { onDateSelected(dateItem) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_xsmall))
                        .shadow(if (isSelected) dimensionResource(R.dimen.padding_xsmall) else dimensionResource(R.dimen.pd_null), RoundedCornerShape(dimensionResource(R.dimen.pd_ten)))
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(dimensionResource(R.dimen.pd_ten))
                        )
                        .padding(horizontal = dimensionResource(R.dimen.smallSpacer), vertical = dimensionResource(R.dimen.padding_small))
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
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                )
            }
        }
    }
}