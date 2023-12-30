package com.example.strikescore.network.standings

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.GET

/**
 * Interface representing the Team API Service, responsible for interacting with the
 * backend API to retrieve information about standings.
 */
interface StandingsApiService {
    /**
     * Retrieves the current standings from the API.
     *
     * @return An [ApiResponseStandings] object containing a list of [ApiResponseTable] objects.
     */
    @GET("standings")
    suspend fun getStandings(): ApiResponseStandings
}

/**
 * Extension function to convert the result of [getStandings] to a [Flow] of [ApiStandings] objects.
 *
 * @return A [Flow] emitting a list of [ApiStandings] objects.
 */
fun StandingsApiService.getStandingsAsFlow(): Flow<List<ApiStandings>> = flow {
    try {
        emit(getStandings().standings[0].table)
    }
    catch(e: Exception){
        Log.e("API", "getTasksAsFlow: "+e.stackTraceToString(), )
    }
}