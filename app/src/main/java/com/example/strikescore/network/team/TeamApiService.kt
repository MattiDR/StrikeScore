package com.example.strikescore.network.team

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.GET

// create the actual function implementations (expensive!)
// no longer needed --> moved to the AppContainer
// object TaskApi{
//
// }

// define what the API looks like
interface TeamApiService {
    // suspend is added to force the user to call this in a coroutine scope
    @GET("teams")
    suspend fun getTasks(): ApiResponseTeams
}

// helper function
fun TeamApiService.getTasksAsFlow(): Flow<List<ApiTeam>> = flow {
    try {
        emit(getTasks().teams)
    }
    catch(e: Exception){
        Log.e("API", "getTasksAsFlow: "+e.stackTraceToString(), )
    }
}