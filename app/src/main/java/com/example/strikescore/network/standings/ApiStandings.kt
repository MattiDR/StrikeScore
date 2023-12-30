package com.example.strikescore.network.standings

import com.example.strikescore.model.Standings
import com.example.strikescore.model.Team
import com.example.strikescore.network.team.ApiTeam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

/**
 * Represents the response from the API containing a list of [ApiResponseTable] objects.
 *
 * @property standings The list of API standings tables.
 */
@Serializable
data class ApiResponseStandings(
    val standings: List<ApiResponseTable>
)

/**
 * Represents a table within the API response containing a list of [ApiStandings] objects.
 *
 * @property table The list of API standings for teams.
 */
@Serializable
data class ApiResponseTable(
    val table: List<ApiStandings>
)

/**
 * Represents the standings of a team retrieved from the API.
 *
 * @property position The current position of the team in the standings.
 * @property team The team associated with the standings.
 * @property playedGames The total number of games played by the team.
 * @property won The number of games won by the team.
 * @property draw The number of games drawn by the team.
 * @property lost The number of games lost by the team.
 * @property points The total points accumulated by the team.
 * @property goalsFor The total number of goals scored by the team.
 * @property goalsAgainst The total number of goals conceded by the team.
 * @property goalDifference The goal difference (goals scored minus goals conceded) for the team.
 */
@Serializable
data class ApiStandings(
    val position: Int,
    val team: ApiTeam,
    val playedGames: Int,
    val won: Int,
    val draw: Int,
    val lost: Int,
    val points: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int,
)

/**
 * Extension function to convert a [Flow] of [ApiStandings] objects to a [Flow] of [Standings] objects.
 *
 * @return A [Flow] of [Standings] objects.
 */
fun Flow<List<ApiStandings>>.asDomainObjects(): Flow<List<Standings>> {
    return map {
        it.asDomainObjects()
    }
}

/**
 * Extension function to convert a list of [ApiStandings] objects to a list of [Standings] objects.
 *
 * @return A list of [Standings] objects.
 */
fun List<ApiStandings>.asDomainObjects(): List<Standings> {
    var domainList = this.map {
        Standings(
            position = it.position,
            team = Team(
                it.team.name,
                it.team.tla,
                it.team.crest,
            ),
            playedGames = it.playedGames,
            won = it.won,
            draw = it.draw,
            lost = it.lost,
            points = it.points,
            goalsFor = it.goalsFor,
            goalsAgainst = it.goalsAgainst,
            goalDifference = it.goalDifference,
        )
    }
    return domainList
}