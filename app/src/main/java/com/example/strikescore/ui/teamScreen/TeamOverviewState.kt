package com.example.strikescore.ui.teamScreen

import androidx.work.WorkInfo
import com.example.strikescore.model.Team


/**
 * Represents the state for the team overview screen.
 *
 * @property newName The team's name.
 * @property newTla The team's three letter abbreviation.
 * @property newCrest The team's crest URL.
 * @property scrollActionIdx Index used for scroll action tracking.
 * @property scrollToItemIndex Index to scroll to within the list.
 */
data class TeamOverviewState(

    val newName: String = "",
    val newTla: String = "",
    val newCrest: String = "",

    val scrollActionIdx: Int = 0,
    val scrollToItemIndex: Int = 0,

)

/**
 * Represents the state for the list of teams.
 *
 * @property teamList The list of teams.
 */
data class TeamListState(val teamList: List<Team> = listOf())

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
 * Represents the state of the team API using the Loading, Content, Error (LCE) pattern.
 * This sealed interface has three possible values: [Success], [Error], and [Loading].
 */
sealed interface TeamApiState {

    /**
     * Represents a successful state in the team API.
     */
    object Success : TeamApiState

    /**
     * Represents an error state in the team API.
     */
    object Error : TeamApiState

    /**
     * Represents a loading state in the team API.
     */
    object Loading : TeamApiState
}