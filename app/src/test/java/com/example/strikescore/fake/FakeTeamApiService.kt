package com.example.strikescore.fake

import com.example.strikescore.network.team.ApiResponseTeams
import com.example.strikescore.network.team.TeamApiService

class FakeTeamApiService : TeamApiService {
    override suspend fun getTasks(): ApiResponseTeams {
        return ApiResponseTeams(FakeDataSource.apiTeams)
    }
}