package com.example.strikescore.data

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.strikescore.data.database.standings.StandingsDao
import com.example.strikescore.data.database.standings.asDbStandings
import com.example.strikescore.data.database.standings.asDomainStandings
import com.example.strikescore.model.Standings
import com.example.strikescore.network.standings.StandingsApiService
import com.example.strikescore.network.standings.asDomainObjects
import com.example.strikescore.network.standings.getStandingsAsFlow
import com.example.strikescore.workerUtils.WifiNotificationWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.net.SocketTimeoutException
import java.util.UUID

interface StandingsRepository {

    // all items from datasource
    fun getStandings(): Flow<List<Standings>>

    // one specific item
    fun getStandings(id: Int): Flow<Standings?>

    suspend fun insertStandings(standings: Standings)

    suspend fun deleteStandings(standings: Standings)

    suspend fun updateStandings(standings: Standings)

    suspend fun refresh()

    var wifiWorkInfo: Flow<WorkInfo>

}

class CachingStandingsRepository(private val standingsDao: StandingsDao, private val standingsApiService: StandingsApiService, context: Context) : StandingsRepository {

    // this repo contains logic to refresh the tasks (remote)
    // sometimes that logic is written in a 'usecase'
    override fun getStandings(): Flow<List<Standings>> {
        // checkes the array of items comming in
        // when empty --> tries to fetch from API
        // clear the DB if inspector is broken...
        /*runBlocking { taskDao.getAllItems().collect{
            for(t: dbTask in it)
                taskDao.delete(t)
        } }*/
        return standingsDao.getAllItems().map {
            it.asDomainStandings()
        }.onEach {
            // todo: check when refresh is called (why duplicates??)
            if (it.isEmpty()) {
                refresh()
            }
        }
    }

    override fun getStandings(position: Int): Flow<Standings?> {
        return standingsDao.getItem(position).map {
            it.asDomainStandings()
        }
    }

    override suspend fun insertStandings(standings: Standings) {
        standingsDao.insert(standings.asDbStandings())
    }

    override suspend fun deleteStandings(standings: Standings) {
        standingsDao.delete(standings.asDbStandings())
    }

    override suspend fun updateStandings(standings: Standings) {
        standingsDao.update(standings.asDbStandings())
    }

    private var workID = UUID(1, 2)

    //the manager is private to the repository
    private val workManager = WorkManager.getInstance(context)

    //the info function is public
    override var wifiWorkInfo: Flow<WorkInfo> =
        workManager.getWorkInfoByIdFlow(workID)

    override suspend fun refresh() {
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
            standingsApiService.getStandingsAsFlow().asDomainObjects().collect { value ->
                for (standing in value) {
                    Log.i("TEST", "refresh: $value")
                    insertStandings(standing)
                }
            }
        } catch (e: SocketTimeoutException) {
            // log something
        }
    }
}