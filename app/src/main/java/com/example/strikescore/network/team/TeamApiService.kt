package com.example.strikescore.network.team

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.GET
/**
 * Interface representing the Team API Service, responsible for interacting with the
 * backend API to retrieve information about teams.
 */
interface TeamApiService {
    /**
     * Retrieves a list of teams from the API.
     *
     * @return An [ApiResponseTeams] object containing a list of [ApiTeam] objects.
     */
    @GET("teams")
    suspend fun getTasks(): ApiResponseTeams
}

/**
 * Extension function to convert the result of [getTasks] to a [Flow] of [ApiTeam] objects.
 *
 * @return A [Flow] emitting a list of [ApiTeam] objects.
 */
fun TeamApiService.getTasksAsFlow(): Flow<List<ApiTeam>> = flow {
    try {
        emit(getTasks().teams)
    }
    catch(e: Exception){
        Log.e("API", "getTasksAsFlow: "+e.stackTraceToString(), )
    }
}