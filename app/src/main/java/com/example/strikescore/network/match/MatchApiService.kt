package com.example.strikescore.network.match

import android.util.Log
import com.example.strikescore.network.standings.ApiResponseStandings
import com.example.strikescore.network.standings.ApiStandings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.GET
interface MatchApiService {
    // suspend is added to force the user to call this in a coroutine scope
    @GET("matches?dateFrom=2023-12-28&dateTo=2023-12-28")
    suspend fun getMatches(): ApiResponseMatch
}

// helper function
fun MatchApiService.getMatchesAsFlow(): Flow<List<ApiMatch>> = flow {
    try {
        emit(getMatches().matches)
    }
    catch(e: Exception){
        Log.e("API", "getTasksAsFlow: "+e.stackTraceToString(), )
    }
}