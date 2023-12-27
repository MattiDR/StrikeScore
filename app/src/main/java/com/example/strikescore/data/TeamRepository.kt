package com.example.strikescore.data


import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.strikescore.data.database.TeamDao
import com.example.strikescore.data.database.asDbTeam
import com.example.strikescore.data.database.asDomainTeams
import com.example.strikescore.data.database.asDomainTeam
import com.example.strikescore.data.database.asDomainTeam
import com.example.strikescore.model.Team
import com.example.strikescore.network.TeamApiService
import com.example.strikescore.network.asDomainObjects
import com.example.strikescore.network.getTasksAsFlow
import com.example.strikescore.workerUtils.WifiNotificationWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.net.SocketTimeoutException
import java.util.UUID

interface TeamRepository {
    // all items from datasource
    fun getTeams(): Flow<List<Team>>

    // one specific item
    fun getTeam(id: String): Flow<Team?>

    suspend fun insertTeam(team: Team)

    suspend fun deleteTeam(team: Team)

    suspend fun updateTeam(team: Team)

    suspend fun refresh()

    var wifiWorkInfo: Flow<WorkInfo>
}

class CachingTeamsRepository(private val teamDao: TeamDao, private val teamApiService: TeamApiService, context: Context) : TeamRepository {

    // this repo contains logic to refresh the tasks (remote)
    // sometimes that logic is written in a 'usecase'
    override fun getTeams(): Flow<List<Team>> {
        // checkes the array of items comming in
        // when empty --> tries to fetch from API
        // clear the DB if inspector is broken...
        /*runBlocking { taskDao.getAllItems().collect{
            for(t: dbTask in it)
                taskDao.delete(t)
        } }*/
        return teamDao.getAllItems().map {
            it.asDomainTeams()
        }.onEach {
            // todo: check when refresh is called (why duplicates??)
            if (it.isEmpty()) {
                refresh()
            }
        }
    }

    override fun getTeam(name: String): Flow<Team?> {
        return teamDao.getItem(name).map {
            it.asDomainTeam()
        }
    }

    override suspend fun insertTeam(team: Team) {
        teamDao.insert(team.asDbTeam())
    }

    override suspend fun deleteTeam(team: Team) {
        teamDao.delete(team.asDbTeam())
    }

    override suspend fun updateTeam(team: Team) {
        teamDao.update(team.asDbTeam())
    }

    private var workID = UUID(1,2)
    //the manager is private to the repository
    private val workManager = WorkManager.getInstance(context)
    //the info function is public
    override var wifiWorkInfo: Flow<WorkInfo> =
        workManager.getWorkInfoByIdFlow(workID)

    override suspend fun refresh() {
        //refresh is used to schedule the workrequest

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val requestBuilder = OneTimeWorkRequestBuilder<WifiNotificationWorker>()
        val request = requestBuilder.setConstraints(constraints).build()
        workManager.enqueue(request)
        workID = request.id
        wifiWorkInfo = workManager.getWorkInfoByIdFlow(request.id)



        //note the actual api request still uses coroutines
        try {
            teamApiService.getTasksAsFlow().asDomainObjects().collect {
                    value ->
                for (team in value) {
                    Log.i("TEST", "refresh: $value")
                    insertTeam(team)
                }
            }
        } catch (e: SocketTimeoutException) {
            // log something
        }
    }
}