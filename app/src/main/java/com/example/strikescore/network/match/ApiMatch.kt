package com.example.strikescore.network.match

import com.example.strikescore.data.database.matches.dbMatch
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


@Serializable
data class ApiResponseMatch(
    val matches: List<ApiMatch>
)

@Serializable
data class ApiMatch(
    val id: Int,
    @Contextual
    val utcDate: String,
    val status: String,
    val matchday: Int,
    val homeTeam: ApiTeam,
    val awayTeam: ApiTeam,
//    val score: ApiScore,
)

fun Flow<List<ApiMatch>>.asDomainObjects(): Flow<List<Match>> {
    return map {
        it.asDomainObjects()
    }
}

// extension function for an ApiTask List to convert is to a Domain Task List
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

        )
    }
    return domainList
}
