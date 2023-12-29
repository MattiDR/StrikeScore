package com.example.strikescore.network.team

import com.example.strikescore.model.Team
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

/**
 * Represents the response from the API containing a list of [ApiTeam] objects.
 *
 * @property teams The list of API teams.
 */
@Serializable
data class ApiResponseTeams(
    val teams: List<ApiTeam>
)

/**
 * Represents a team retrieved from the API.
 *
 * @property name The name of the team.
 * @property tla The three-letter acronym representing the team.
 * @property crest The URL or path to the team's crest or logo.
 */
@Serializable
data class ApiTeam(
    val name: String,
    val tla: String,
    val crest: String,
)

/**
 * Extension function to convert a [Flow] of [ApiTeam] objects to a [Flow] of [Team] objects.
 *
 * @return A [Flow] of [Team] objects.
 */
fun Flow<List<ApiTeam>>.asDomainObjects(): Flow<List<Team>> {
    return map {
        it.asDomainObjects()
    }
}

/**
 * Extension function to convert a list of [ApiTeam] objects to a list of [Team] objects.
 *
 * @return A list of [Team] objects.
 */
fun List<ApiTeam>.asDomainObjects(): List<Team> {
    var domainList = this.map {
        Team(it.name, it.tla, it.crest)
    }
    return domainList
}