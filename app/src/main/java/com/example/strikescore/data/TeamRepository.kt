package com.example.strikescore.data


import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.strikescore.data.database.team.TeamDao
import com.example.strikescore.data.database.team.asDbTeam
import com.example.strikescore.data.database.team.asDomainTeams
import com.example.strikescore.data.database.team.asDomainTeam
import com.example.strikescore.model.Team
import com.example.strikescore.network.team.TeamApiService
import com.example.strikescore.network.team.asDomainObjects
import com.example.strikescore.network.team.getTasksAsFlow
import com.example.strikescore.workerUtils.WifiNotificationWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.net.SocketTimeoutException
import java.util.UUID

/**
 * A repository interface for managing Team data.
 *
 * This interface defines methods for retrieving, inserting, deleting, updating, and refreshing Team
 * objects. It also provides a flow of WorkInfo for tracking the status of background work.
 */
interface TeamRepository {

    /**
     * Retrieves all teams from the data source.
     *
     * @return A flow emitting a list of Team objects.
     */
    fun getTeams(): Flow<List<Team>>

    /**
     * Retrieves a specific team by its name.
     *
     * @param name The name of the team to retrieve.
     * @return A flow emitting the requested Team object or null if not found.
     */
    fun getTeam(id: String): Flow<Team?>

/**
     * Inserts a new team into the repository.
     *
     * @param team The Team object to be inserted.
     */
    suspend fun insertTeam(team: Team)

    /**
     * Deletes a team from the repository.
     *
     * @param team The Team object to be deleted.
     */
    suspend fun deleteTeam(team: Team)

    /**
     * Updates an existing team in the repository.
     *
     * @param team The Team object to be updated.
     */
    suspend fun updateTeam(team: Team)

    /**
     * Refreshes the team data by triggering a background task.
     */
    suspend fun refresh()

    /**
     * A flow of WorkInfo representing the status of the Wi-Fi notification background task.
     */
    var wifiWorkInfo: Flow<WorkInfo>
}

/**
 * An implementation of the TeamRepository interface that provides caching and remote data retrieval
 * using WorkManager.
 *
 * @param teamDao The DAO (Data Access Object) for Team entities.
 * @param teamApiService The service for fetching team data from a remote source.
 * @param context The application context.
 */
class CachingTeamsRepository(private val teamDao: TeamDao, private val teamApiService: TeamApiService, context: Context) : TeamRepository {


    /**
     * Retrieves all teams from the data source. If no teams are found in the local database, it
     * triggers a remote data refresh using the refresh method.
     *
     * @return A flow emitting a list of Team objects.
     */
    override fun getTeams(): Flow<List<Team>> {
        return teamDao.getAllItems().map {
            it.asDomainTeams()
        }.onEach {
            if (it.isEmpty()) {
                refresh()
            }
        }
    }

    /**
     * Retrieves a specific team by its name.
     *
     * @param name The name of the team to retrieve.
     * @return A flow emitting the requested Team object or null if not found.
     */
    override fun getTeam(name: String): Flow<Team?> {
        return teamDao.getItem(name).map {
            it.asDomainTeam()
        }
    }

    /**
     * Inserts a new team entry into the local database.
     *
     * @param team The Team object to be inserted.
     */
    override suspend fun insertTeam(team: Team) {
        teamDao.insert(team.asDbTeam())
    }

    /**
     * Deletes a team from the local database.
     *
     * @param team The Team object to be deleted.
     */
    override suspend fun deleteTeam(team: Team) {
        teamDao.delete(team.asDbTeam())
    }

    /**
     * Updates an existing team in the local database.
     *
     * @param team The Team object to be updated.
     */
    override suspend fun updateTeam(team: Team) {
        teamDao.update(team.asDbTeam())
    }

    // Internal ID for tracking the Wi-Fi notification background task.
    private var workID = UUID(1,2)
    // WorkManager instance for managing background tasks.
    private val workManager = WorkManager.getInstance(context)
    // A flow of WorkInfo representing the status of the Wi-Fi notification background task.
    override var wifiWorkInfo: Flow<WorkInfo> =
        workManager.getWorkInfoByIdFlow(workID)


    /**
     * Refreshes the team data by scheduling a Wi-Fi dependent background task. This method enqueues
     * a OneTimeWorkRequest for the Wi-FiNotificationWorker and updates the wifiWorkInfo flow to
     * track the status of the background task.
     */
    override suspend fun refresh() {

        // Set constraints for the background task to require a connected network.
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        // Build a OneTimeWorkRequest for the Wi-FiNotificationWorker.
        val requestBuilder = OneTimeWorkRequestBuilder<WifiNotificationWorker>()
        val request = requestBuilder.setConstraints(constraints).build()
        // Enqueue the work request and update the workID and wifiWorkInfo flow.
        workManager.enqueue(request)
        workID = request.id
        wifiWorkInfo = workManager.getWorkInfoByIdFlow(request.id)



        // Use coroutines to perform the actual API request for team data.
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