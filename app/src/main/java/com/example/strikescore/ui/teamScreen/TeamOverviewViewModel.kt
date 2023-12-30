package com.example.strikescore.ui.teamScreen

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.strikescore.StrikeScoreApplication
import com.example.strikescore.data.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException


/**
 * ViewModel for the Team Overview screen.
 *
 * @param teamsRepository The repository responsible for handling team-related data.
 */
class TeamOverviewViewModel(private val teamsRepository: TeamRepository) : ViewModel() {
    // use StateFlow (Flow: emits current state + any updates)
    /*
    * Note: uiState is a cold flow. Changes don't come in from above unless a
    * refresh is called...
    * */
    private val _uiState = MutableStateFlow(TeamOverviewState())
    val uiState: StateFlow<TeamOverviewState> = _uiState.asStateFlow()

    /*
    * Note: uiListState is a hot flow (.stateIn makes it so) --> it updates given a scope (viewmodelscope)
    * when no updates are required (lifecycle) the subscription is stopped after a timeout
    * */
    lateinit var uiListState: StateFlow<TeamListState>

    // keeping the state of the api request
    var teamApiState: TeamApiState by mutableStateOf(TeamApiState.Loading)
        private set

    // state of the workers, prepared here for the UI
    //note, a better approach would use a new data class to represent the state...
    lateinit var wifiWorkerState: StateFlow<WorkerState>

    init {

        // initializes the uiListState
        getRepoTeams()
        Log.i("vm inspection", "TeamOverviewViewModel init")



    }


    /**
     * updates repository teams and UI states based on the specified date.
     *
     * 1. Refreshes match repository.
     * 2. Updates [uiListState] and [wifiWorkerState].
     * 3. Sets [standingsApiState] to [Success] or [Error] on API request result.
     */
    private fun getRepoTeams() {
        try {
            viewModelScope.launch { teamsRepository.refresh() }

            uiListState = teamsRepository.getTeams().map { TeamListState(it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000L),
                    initialValue = TeamListState(),
                )
            teamApiState = TeamApiState.Success

            wifiWorkerState = teamsRepository.wifiWorkInfo.map { WorkerState(it)}.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = WorkerState(),
            )
        } catch (e: IOException) {
            // show a toast? save a log on firebase? ...
            // set the error state
            teamApiState = TeamApiState.Error
        }
    }


    // object to tell the android framework how to handle the parameter of the viewmodel
    companion object {
        private var Instance: TeamOverviewViewModel? = null
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                if (Instance == null) {
                    val application = (this[APPLICATION_KEY] as StrikeScoreApplication)
                    val teamsRepository = application.container.teamsRepository
                    Instance = TeamOverviewViewModel(teamsRepository = teamsRepository)
                }
                Instance!!
            }
        }
    }
}