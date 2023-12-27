package com.example.strikescore.network.standings

import com.example.strikescore.data.database.team.asDomainTeam
import com.example.strikescore.data.database.team.dbTeam
import com.example.strikescore.model.Standings
import com.example.strikescore.model.Team
import com.example.strikescore.network.team.ApiTeam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponsetest(
    val standings: List<ApiResponseStandings>
)

@Serializable
data class ApiResponseStandings(
    val table: List<ApiStandings>
)
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

// extension function for an ApiTask List to convert is to a Domain Task List
fun Flow<List<ApiStandings>>.asDomainObjects(): Flow<List<Standings>> {
    return map {
        it.asDomainObjects()
    }
}

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