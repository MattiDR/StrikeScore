package com.example.strikescore.fake

import androidx.work.WorkInfo
import com.example.strikescore.data.TeamRepository
import com.example.strikescore.data.database.team.asDomainTeams
import com.example.strikescore.model.Team
import com.example.strikescore.network.team.asDomainObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FakeTeamRepository : TeamRepository {

    override fun getTeams(): Flow<List<Team>> {
        return flow {
            // Emit your list of teams here
            emit(FakeDataSource.apiTeams.asDomainObjects())
        }
    }

    override fun getTeam(id: String): Flow<Team?> {
        TODO("Not yet implemented")
    }

    override suspend fun insertTeam(team: Team) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTeam(team: Team) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTeam(team: Team) {
        TODO("Not yet implemented")
    }

    override suspend fun refresh() {
        TODO("Not yet implemented")
    }

    override var wifiWorkInfo: Flow<WorkInfo>
        get() = TODO("Not yet implemented")
        set(value) {}
}