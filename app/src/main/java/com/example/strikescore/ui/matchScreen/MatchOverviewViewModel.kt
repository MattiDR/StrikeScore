package com.example.strikescore.ui.matchScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.strikescore.StrikeScoreApplication
import com.example.strikescore.data.MatchRepository
import com.example.strikescore.ui.standingsScreen.WorkerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

class MatchOverviewViewModel(private val matchRepository: MatchRepository) : ViewModel() {
    // use StateFlow (Flow: emits current state + any updates)
    /*
    * Note: uiState is a cold flow. Changes don't come in from above unless a
    * refresh is called...
    * */
    private val _uiState = MutableStateFlow(MatchOverviewState(/*TaskSampler.getAll()*/))
    val uiState: StateFlow<MatchOverviewState> = _uiState.asStateFlow()

    /*
    * Note: uiListState is a hot flow (.stateIn makes it so) --> it updates given a scope (viewmodelscope)
    * when no updates are required (lifecycle) the subscription is stopped after a timeout
    * */
    lateinit var uiListState: StateFlow<MatchListState>

    // keeping the state of the api request
    var matchApiState: MatchApiState by mutableStateOf(MatchApiState.Loading)
        private set

    // state of the workers, prepared here for the UI
    //note, a better approach would use a new data class to represent the state...
    lateinit var wifiWorkerState: StateFlow<WorkerState>

    init {

        // initializes the uiListState
        getRepoTasks()
        Log.i("vm inspection", "TeamOverviewViewModel init")



    }


    // this
    private fun getRepoTasks() {
        try {
            viewModelScope.launch { matchRepository.refresh() }

            uiListState = matchRepository.getMatch().map { MatchListState(it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000L),
                    initialValue = MatchListState(),
                )
            matchApiState = MatchApiState.Success

            wifiWorkerState = matchRepository.wifiWorkInfo.map { WorkerState(it) }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = WorkerState(),
            )
        } catch (e: IOException) {
            // show a toast? save a log on firebase? ...
            // set the error state
            matchApiState = MatchApiState.Error
        }
    }


    // object to tell the android framework how to handle the parameter of the viewmodel
    companion object {
        private var Instance: MatchOverviewViewModel? = null
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                if (Instance == null) {
                    val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StrikeScoreApplication)
                    val matchRepository = application.container.matchRepository
                    Instance = MatchOverviewViewModel(matchRepository = matchRepository)
                }
                Instance!!
            }
        }
    }
}