package com.example.strikescore.network.match

import android.util.Log
import com.example.strikescore.network.standings.ApiResponseStandings
import com.example.strikescore.network.standings.ApiStandings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.GET
import retrofit2.http.Query

interface MatchApiService {
    // suspend is added to force the user to call this in a coroutine scope
    @GET("matches")
    suspend fun getMatches(
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): ApiResponseMatch
}

// helper function
fun MatchApiService.getMatchesAsFlow(date: String): Flow<List<ApiMatch>> = flow {
    try {
        emit(getMatches(date, date).matches)
    }
    catch(e: Exception){
        Log.e("API", "getTasksAsFlow: "+e.stackTraceToString(), )
    }
}