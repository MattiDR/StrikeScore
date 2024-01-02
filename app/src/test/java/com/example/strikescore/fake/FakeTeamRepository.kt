package com.example.strikescore.fake

import androidx.work.WorkInfo
import com.example.strikescore.data.TeamRepository
import com.example.strikescore.model.Team
import com.example.strikescore.network.team.asDomainObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mockito.Mockito.mock

class FakeTeamRepository : TeamRepository {

    private var _wifiWorkInfo: Flow<WorkInfo>? = null

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
       //
    }

    override var wifiWorkInfo: Flow<WorkInfo>
        get() = _wifiWorkInfo ?:
        flow{
            emit(mock(WorkInfo::class.java))
        }
        set(value) { _wifiWorkInfo = value }
}