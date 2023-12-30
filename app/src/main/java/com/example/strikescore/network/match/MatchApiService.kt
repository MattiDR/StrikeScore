package com.example.strikescore.network.match

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface representing the Team API Service, responsible for interacting with the
 * backend API to retrieve information about matches.
 */
interface MatchApiService {
    /**
     * Retrieves a list of matches from the API within the specified date range.
     *
     * @param dateFrom The starting date for the match query.
     * @param dateTo The ending date for the match query.
     * @return An [ApiResponseMatch] object containing a list of [ApiMatch] objects.
     */
    @GET("matches")
    suspend fun getMatches(
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): ApiResponseMatch
}

/**
 * Extension function to convert the result of [getMatches] to a [Flow] of [ApiMatch] objects.
 *
 * @param date The date parameter for the match query.
 * @return A [Flow] emitting a list of [ApiMatch] objects.
 */
fun MatchApiService.getMatchesAsFlow(date: String): Flow<List<ApiMatch>> = flow {
    try {
        emit(getMatches(date, date).matches)
    }
    catch(e: Exception){
        Log.e("API", "getTasksAsFlow: "+e.stackTraceToString(), )
    }
}