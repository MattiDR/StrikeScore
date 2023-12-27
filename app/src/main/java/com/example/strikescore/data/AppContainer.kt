package com.example.strikescore.data

import android.content.Context
import com.example.strikescore.data.database.TeamDb
import com.example.strikescore.network.NetworkConnectionInterceptor
import com.example.strikescore.network.TeamApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface AppContainer {
    val teamsRepository: TeamRepository
}

// container that takes care of dependencies
class DefaultAppContainer(private val context: Context) : AppContainer {

    private val networkCheck = NetworkConnectionInterceptor(context)
    private val client = OkHttpClient.Builder()
        .addInterceptor(networkCheck)
        .build()


    private val baseUrl = "https://api.football-data.org/v4/competitions/PL/"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(
            Json { ignoreUnknownKeys = true}.asConverterFactory("application/json".toMediaType()),
        )
        .baseUrl(baseUrl)
        .client(client)
        .build()

    private val retrofitService: TeamApiService by lazy {
        retrofit.create(TeamApiService::class.java)
    }

    /*
    override val tasksRepository: TasksRepository by lazy {
        ApiTasksRepository(retrofitService)
    }
    */
    override val teamsRepository: TeamRepository by lazy {
        CachingTeamsRepository(TeamDb.getDatabase(context = context).teamDao(), retrofitService, context)
    }
}