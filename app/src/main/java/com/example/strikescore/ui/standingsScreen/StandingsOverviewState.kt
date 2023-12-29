package com.example.strikescore.ui.standingsScreen

import androidx.work.WorkInfo
import com.example.strikescore.model.Standings
import com.example.strikescore.model.Team
import com.example.strikescore.ui.matchScreen.MatchApiState.Error
import com.example.strikescore.ui.matchScreen.MatchApiState.Loading
import com.example.strikescore.ui.matchScreen.MatchApiState.Success
import java.text.FieldPosition

/**
 * Represents the state for the standings overview screen.
 *
 * @property position The team's position in the standings.
 * @property team The team's details.
 * @property playedGames The number of games played.
 * @property won The number of games won.
 * @property draw The number of games drawn.
 * @property lost The number of games lost.
 * @property points The total points.
 * @property goalsFor The number of goals scored.
 * @property goalsAgainst The number of goals conceded.
 * @property goalDifference The goal difference (goals scored - goals conceded).
 * @property scrollActionIdx Index used for scroll action tracking.
 * @property scrollToItemIndex Index to scroll to within the list.
 */
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

/**
 * Represents the state for the list of standings.
 *
 * @property standingsList The list of standings.
 */
data class StandingsListState(val standingsList: List<Standings> = listOf())


/**
 * Represents the state for the background worker handling Wi-Fi connectivity.
 *
 * @property workerInfo The information about the background worker's state.
 */
data class WorkerState(val workerInfo: WorkInfo? = null)

// the sealed interface has only three possible values
/*Sidenote: to learn more about this TaskApiState object, you can search on LCE (Loading, Content, Error) pattern

When the state is changed to Error, the taskList will not be updated (offline first).
To ensure the list is considered immutable (fully immutable, won't ever change unless a new object is created), add the Immutable annotation.

The LCE pattern is not completed in the application, because it requires more complex helper classes
An example can be found here https://www.valueof.io/blog/compose-ui-state-flow-offline-first-repository
*/
/**
 * Represents the state of the standings API using the Loading, Content, Error (LCE) pattern.
 * This sealed interface has three possible values: [Success], [Error], and [Loading].
 */
sealed interface StandingsApiState {
    /**
     * Represents a successful state in the standings API.
     */
    object Success : StandingsApiState

    /**
     * Represents an error state in the standings API.
     */
    object Error : StandingsApiState

    /**
     * Represents a loading state in the standings API.
     */
    object Loading : StandingsApiState
}