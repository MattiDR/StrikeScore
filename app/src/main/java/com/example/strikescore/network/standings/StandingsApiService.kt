package com.example.strikescore.network.standings

import android.util.Log
import com.example.strikescore.network.team.ApiTeam
import com.example.strikescore.network.team.TeamApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.GET
interface StandingsApiService {
    // suspend is added to force the user to call this in a coroutine scope
    @GET("standings")
    suspend fun getStandings(): ApiResponsetest
}

// helper function
fun StandingsApiService.getStandingsAsFlow(): Flow<List<ApiStandings>> = flow {
    try {
        emit(getStandings().standings[0].table)
    }
    catch(e: Exception){
        Log.e("API", "getTasksAsFlow: "+e.stackTraceToString(), )
    }
}