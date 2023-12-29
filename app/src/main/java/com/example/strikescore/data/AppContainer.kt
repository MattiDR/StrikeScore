package com.example.strikescore.data

import android.content.Context
import com.example.strikescore.data.database.StrikeScoreDb
import com.example.strikescore.network.NetworkConnectionInterceptor
import com.example.strikescore.network.match.MatchApiService
import com.example.strikescore.network.standings.StandingsApiService
import com.example.strikescore.network.team.TeamApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Interface defining the dependencies needed by the app.
 */
interface AppContainer {
    val teamsRepository: TeamRepository
    val standingsRepository: StandingsRepository
    val matchRepository: MatchRepository
}

/**
 * Default implementation of [AppContainer] providing the necessary dependencies.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    // Network dependencies
    private val networkCheck = NetworkConnectionInterceptor(context)
    private val client = OkHttpClient.Builder()
        .addInterceptor(networkCheck)
        .build()

    // Retrofit configuration
    private val baseUrl = "https://api.football-data.org/v4/competitions/PL/"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(
            Json { ignoreUnknownKeys = true}.asConverterFactory("application/json".toMediaType()),
        )
        .baseUrl(baseUrl)
        .client(client)
        .build()

    // Retrofit services
    private val retrofitTeamService: TeamApiService by lazy {
        retrofit.create(TeamApiService::class.java)
    }
    private val retrofitStandingsService: StandingsApiService by lazy {
        retrofit.create(StandingsApiService::class.java)
    }

    private val retrofitMatchService: MatchApiService by lazy {
        retrofit.create(MatchApiService::class.java)
    }

    // Repositories
    override val teamsRepository: TeamRepository by lazy {
        CachingTeamsRepository(StrikeScoreDb.getDatabase(context = context).teamDao(), retrofitTeamService, context)
    }

    override val standingsRepository: StandingsRepository by lazy {
        CachingStandingsRepository(StrikeScoreDb.getDatabase(context = context).standingsDao(), retrofitStandingsService, context)
    }

    override val matchRepository: MatchRepository by lazy {
        CachingMatchRepository(StrikeScoreDb.getDatabase(context = context).matchDao(), retrofitMatchService, context)
    }
}