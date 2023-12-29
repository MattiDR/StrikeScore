package com.example.strikescore.ui.matchScreen

import androidx.work.WorkInfo
import com.example.strikescore.model.Match
import com.example.strikescore.model.Standings
import com.example.strikescore.model.Team

/**
 * Represents the state of a match overview screen.
 *
 * @property id The unique identifier of the match.
 * @property homeTeam The home team participating in the match.
 * @property awayTeam The away team participating in the match.
 * @property status The current status of the match.
 * @property matchday The matchday number.
 * @property utcDate The UTC date of the match.
 * @property scrollActionIdx The index of the scroll action.
 * @property scrollToItemIndex The index to which the screen should scroll.
 */
data class MatchOverviewState(

    val id : Int = 0,
    val homeTeam: Team =  Team("", "", ""),
    val awayTeam: Team =  Team("", "", ""),
    val status: String = "",
    val matchday: Int = 0,
    val utcDate: String = "",
//    val score: Score = Score(),



    val scrollActionIdx: Int = 0,
    val scrollToItemIndex: Int = 0,

    )

/**
 * Represents the state of a list of matches.
 *
 * @property matchList The list of matches.
 */
data class MatchListState(val matchList: List<Match> = listOf())
/**
 * Represents the state of a worker, including its information.
 *
 * @property workerInfo The [WorkInfo] associated with the worker.
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
 * Represents the state of the Match API using the Loading, Content, Error (LCE) pattern.
 * This sealed interface has three possible values: [Success], [Error], and [Loading].
 */
sealed interface MatchApiState {
    /**
     * Represents a successful state in the Match API.
     */
    object Success : MatchApiState

    /**
     * Represents an error state in the Match API.
     */
    object Error : MatchApiState

    /**
     * Represents a loading state in the Match API.
     */
    object Loading : MatchApiState
}