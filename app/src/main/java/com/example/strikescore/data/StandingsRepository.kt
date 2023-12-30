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

/**
 * A repository interface for managing Standings data.
 *
 * This interface defines methods for retrieving, inserting, deleting, updating, and refreshing
 * Standings objects. It also provides a flow of WorkInfo for tracking the status of background work.
 */
interface StandingsRepository {

    /**
     * Retrieves all standings from the data source.
     *
     * @return A flow emitting a list of Standings objects.
     */
    fun getStandings(): Flow<List<Standings>>

    /**
     * Retrieves a specific standing by its position.
     *
     * @param id The position of the standing to retrieve.
     * @return A flow emitting the requested Standings object or null if not found.
     */
    fun getStandings(id: Int): Flow<Standings?>

    /**
     * Inserts a new standings entry into the repository.
     *
     * @param standings The Standings object to be inserted.
     */
    suspend fun insertStandings(standings: Standings)

    /**
     * Deletes a standings entry from the repository.
     *
     * @param standings The Standings object to be deleted.
     */
    suspend fun deleteStandings(standings: Standings)

    /**
     * Updates an existing standings entry in the repository.
     *
     * @param standings The updated Standings object.
     */
    suspend fun updateStandings(standings: Standings)

    /**
     * Refreshes the standings data by triggering a background task.
     */
    suspend fun refresh()


    /**
     * A flow of WorkInfo representing the status of the Wi-Fi notification background task.
     */
    var wifiWorkInfo: Flow<WorkInfo>

}

/**
 * An implementation of the StandingsRepository interface that provides caching and remote data
 * retrieval using WorkManager.
 *
 * @param standingsDao The DAO (Data Access Object) for Standings entities.
 * @param standingsApiService The service for fetching standings data from a remote source.
 * @param context The application context.
 */
class CachingStandingsRepository(private val standingsDao: StandingsDao, private val standingsApiService: StandingsApiService, context: Context) : StandingsRepository {


    /**
     * Retrieves all standings from the data source. If no standings are found in the local
     * database, it triggers a remote data refresh using the refresh method.
     *
     * @return A flow emitting a list of Standings objects.
     */
    override fun getStandings(): Flow<List<Standings>> {

        return standingsDao.getAllItems().map {
            it.asDomainStandings()
        }.onEach {
            if (it.isEmpty()) {
                refresh()
            }
        }
    }

    /**
     * Retrieves a specific standing by its position.
     *
     * @param position The position of the standing to retrieve.
     * @return A flow emitting the requested Standings object or null if not found.
     */
    override fun getStandings(position: Int): Flow<Standings?> {
        return standingsDao.getItem(position).map {
            it.asDomainStandings()
        }
    }

    /**
     * Inserts a new standings entry into the local database.
     *
     * @param standings The Standings object to be inserted.
     */
    override suspend fun insertStandings(standings: Standings) {
        standingsDao.insert(standings.asDbStandings())
    }

    /**
     * Deletes a standings entry from the local database.
     *
     * @param standings The Standings object to be deleted.
     */
    override suspend fun deleteStandings(standings: Standings) {
        standingsDao.delete(standings.asDbStandings())
    }

    /**
     * Updates an existing standings entry in the local database.
     *
     * @param standings The updated Standings object.
     */
    override suspend fun updateStandings(standings: Standings) {
        standingsDao.update(standings.asDbStandings())
    }

    // Internal ID for tracking the Wi-Fi notification background task.
    private var workID = UUID(1, 2)

    // WorkManager instance for managing background tasks.
    private val workManager = WorkManager.getInstance(context)

    // A flow of WorkInfo representing the status of the Wi-Fi notification background task.
    override var wifiWorkInfo: Flow<WorkInfo> =
        workManager.getWorkInfoByIdFlow(workID)

    /**
     * Refreshes the standings data by scheduling a Wi-Fi dependent background task. This method
     * enqueues a OneTimeWorkRequest for the Wi-FiNotificationWorker and updates the wifiWorkInfo
     * flow to track the status of the background task.
     */
    override suspend fun refresh() {

        // Set constraints for the background task to require a connected network.
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        // Build a OneTimeWorkRequest for the Wi-FiNotificationWorker.

        val requestBuilder = OneTimeWorkRequestBuilder<WifiNotificationWorker>()
        val request = requestBuilder.setConstraints(constraints).build()

        // Enqueue the work request and update the workID and wifiWorkInfo flow.
        workManager.enqueue(request)
        workID = request.id
        wifiWorkInfo = workManager.getWorkInfoByIdFlow(request.id)


        // Use coroutines to perform the actual API request for standings data.
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