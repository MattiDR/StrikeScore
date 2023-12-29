package com.example.strikescore.network.match

import com.example.strikescore.data.database.matches.DbMatch
import com.example.strikescore.model.FullTime
import com.example.strikescore.model.Match
import com.example.strikescore.model.Standings
import com.example.strikescore.model.Team
import com.example.strikescore.network.standings.ApiStandings
import com.example.strikescore.network.standings.asDomainObjects
import com.example.strikescore.network.team.ApiTeam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.Date


/**
 * Represents the response from the API containing a list of [ApiMatch] objects.
 *
 * @property matches The list of API matches.
 */
@Serializable
data class ApiResponseMatch(
    val matches: List<ApiMatch>
)

/**
 * Represents a match retrieved from the API.
 *
 * @property id The unique identifier for the match.
 * @property utcDate The UTC date and time when the match is scheduled to take place.
 * @property status The current status of the match (e.g., "scheduled", "in progress", "finished").
 * @property matchday The matchday number for the match.
 * @property homeTeam The home team participating in the match.
 * @property awayTeam The away team participating in the match.
 * @property score The scoring details for the match.
 */
@Serializable
data class ApiMatch(
    val id: Int,
    @Contextual
    val utcDate: String,
    val status: String,
    val matchday: Int,
    val homeTeam: ApiTeam,
    val awayTeam: ApiTeam,
    @Contextual
    val score: Score,
)

/**
 * Represents the scoring details for a match in the API response.
 *
 * @property fullTime The full-time scoring details for the match.
 */
@Serializable
data class Score(
    val fullTime: com.example.strikescore.network.match.FullTime,
)

/**
 * Represents the full-time scoring details for a match in the API response.
 *
 * @property home The number of goals scored by the home team (nullable).
 * @property away The number of goals scored by the away team (nullable).
 */
@Serializable
data class FullTime(
    val home: Int?,
    val away: Int?,
)

/**
 * Extension function to convert a [Flow] of [ApiMatch] objects to a [Flow] of [Match] objects.
 *
 * @return A [Flow] of [Match] objects.
 */
fun Flow<List<ApiMatch>>.asDomainObjects(): Flow<List<Match>> {
    return map {
        it.asDomainObjects()
    }
}

/**
 * Extension function to convert a list of [ApiMatch] objects to a list of [Match] objects.
 *
 * @return A list of [Match] objects.
 */
fun List<ApiMatch>.asDomainObjects(): List<Match> {
    var domainList = this.map {
        Match(
            id = it.id,
            homeTeam = Team(
                it.homeTeam.name,
                it.homeTeam.tla,
                it.homeTeam.crest,
            ),
            awayTeam =
                Team(
                    it.awayTeam.name,
                    it.awayTeam.tla,
                    it.awayTeam.crest,
                ),
            status = it.status,
            matchday = it.matchday,
            utcDate = it.utcDate,
            score = com.example.strikescore.model.Score(
                FullTime(
                    home = it.score.fullTime.home,
                    away = it.score.fullTime.away
                )
            )

        )
    }
    return domainList
}
