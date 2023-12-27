package com.example.strikescore.ui.standingsScreen

import androidx.work.WorkInfo
import com.example.strikescore.model.Standings
import com.example.strikescore.model.Team
import java.text.FieldPosition


data class StandingsOverviewState(

    val position: Int = 0,
    val team: Team = Team("", "", ""),
    val playedGames: Int = 0,
    val won: Int = 0,
    val draw: Int = 0,
    val lost: Int = 0,
    val points: Int = 0,
    val goalsFor: Int = 0,
    val goalsAgainst: Int = 0,
    val goalDifference: Int = 0,


    val scrollActionIdx: Int = 0,
    val scrollToItemIndex: Int = 0,

    )

data class StandingsListState(val standingsList: List<Standings> = listOf())

data class WorkerState(val workerInfo: WorkInfo? = null)

// the sealed interface has only three possible values
/*Sidenote: to learn more about this TaskApiState object, you can search on LCE (Loading, Content, Error) pattern

When the state is changed to Error, the taskList will not be updated (offline first).
To ensure the list is considered immutable (fully immutable, won't ever change unless a new object is created), add the Immutable annotation.

The LCE pattern is not completed in the application, because it requires more complex helper classes
An example can be found here https://www.valueof.io/blog/compose-ui-state-flow-offline-first-repository
*/

sealed interface StandingsApiState {
    object Success : StandingsApiState
    object Error : StandingsApiState
    object Loading : StandingsApiState
}