package com.example.strikescore.network.team

import com.example.strikescore.model.Team
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseTeams(
    val teams: List<ApiTeam>
)
@Serializable
data class ApiTeam(
    val name: String,
    val tla: String,
    val crest: String,
)

// extension function for an ApiTask List to convert is to a Domain Task List
fun Flow<List<ApiTeam>>.asDomainObjects(): Flow<List<Team>> {
    return map {
        it.asDomainObjects()
    }
}

fun List<ApiTeam>.asDomainObjects(): List<Team> {
    var domainList = this.map {
        Team(it.name, it.tla, it.crest)
    }
    return domainList
}