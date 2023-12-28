package com.example.strikescore.data

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.strikescore.data.database.matches.MatchDao
import com.example.strikescore.data.database.matches.asDbMatch
import com.example.strikescore.data.database.matches.asDomainMatch
import com.example.strikescore.data.database.matches.asDomainMatches
import com.example.strikescore.data.database.matches.dbMatch
import com.example.strikescore.data.database.standings.StandingsDao
import com.example.strikescore.data.database.standings.asDbStandings
import com.example.strikescore.data.database.standings.asDomainStandings
import com.example.strikescore.model.Match
import com.example.strikescore.model.Standings
import com.example.strikescore.network.match.MatchApiService
import com.example.strikescore.network.match.asDomainObjects
import com.example.strikescore.network.match.getMatchesAsFlow
import com.example.strikescore.network.standings.StandingsApiService
import com.example.strikescore.network.standings.asDomainObjects
import com.example.strikescore.network.standings.getStandingsAsFlow
import com.example.strikescore.workerUtils.WifiNotificationWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import java.net.SocketTimeoutException
import java.util.UUID


interface MatchRepository {

    // all items from datasource
    fun getMatch(date:String): Flow<List<Match>>

    // one specific item
    fun getMatch(id: Int): Flow<Match?>

    suspend fun insertMatch(match: Match)

    suspend fun deleteMatch(match: Match)

    suspend fun updateMatch(match: Match)

    suspend fun refresh(date: String)

    var wifiWorkInfo: Flow<WorkInfo>

}

class CachingMatchRepository(private val matchDao: MatchDao, private val matchApiService: MatchApiService, context: Context) : MatchRepository {

    // this repo contains logic to refresh the tasks (remote)
    // sometimes that logic is written in a 'usecase'
    override fun getMatch(date: String): Flow<List<Match>> {
        // checkes the array of items comming in
        // when empty --> tries to fetch from API
        // clear the DB if inspector is broken...
//        runBlocking { matchDao.getAllItems(dateFrom).collect{
//            for(t: dbMatch in it)
//                matchDao.delete(t)
//        } }
        return matchDao.getAllItems(date).map {
            it.asDomainMatches()
        }.onEach {
            // todo: check when refresh is called (why duplicates??)
            if (it.isEmpty()) {
                refresh(date)
            }
        }
    }

    override fun getMatch(id: Int): Flow<Match?> {
        return matchDao.getItem(id).map {
            it.asDomainMatch()
        }
    }

    override suspend fun insertMatch(match: Match) {
        matchDao.insert(match.asDbMatch())
    }

    override suspend fun deleteMatch(match: Match) {
        matchDao.delete(match.asDbMatch())
    }

    override suspend fun updateMatch(match: Match) {
        matchDao.update(match.asDbMatch())
    }

    private var workID = UUID(1, 2)

    //the manager is private to the repository
    private val workManager = WorkManager.getInstance(context)

    //the info function is public
    override var wifiWorkInfo: Flow<WorkInfo> =
        workManager.getWorkInfoByIdFlow(workID)

    override suspend fun refresh(date: String) {
        //refresh is used to schedule the workrequest

        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val requestBuilder = OneTimeWorkRequestBuilder<WifiNotificationWorker>()
        val request = requestBuilder.setConstraints(constraints).build()
        workManager.enqueue(request)
        workID = request.id
        wifiWorkInfo = workManager.getWorkInfoByIdFlow(request.id)


        //note the actual api request still uses coroutines
        try {
            matchApiService.getMatchesAsFlow(date).asDomainObjects().collect { value ->
                for (standing in value) {
                    Log.i("TEST", "refresh: $value")
                    insertMatch(standing)
                }
            }
        } catch (e: SocketTimeoutException) {
            // log something
        }
    }
}