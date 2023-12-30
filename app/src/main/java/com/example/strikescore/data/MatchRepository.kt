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
import com.example.strikescore.model.Match
import com.example.strikescore.network.match.MatchApiService
import com.example.strikescore.network.match.asDomainObjects
import com.example.strikescore.network.match.getMatchesAsFlow
import com.example.strikescore.workerUtils.WifiNotificationWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.net.SocketTimeoutException
import java.util.UUID

/**
 * A repository interface for managing Match data.
 *
 * This interface defines methods for retrieving, inserting, deleting, updating, and refreshing
 * Match objects. It also provides a flow of WorkInfo for tracking the status of background work.
 */
interface MatchRepository {

    /**
     * Retrieves all matches for a given date.
     *
     * @param date The date for which matches should be retrieved.
     * @return A flow emitting a list of Match objects.
     */
    fun getMatch(date:String): Flow<List<Match>>

    /**
     * Retrieves a specific match by its ID.
     *
     * @param id The ID of the match to retrieve.
     * @return A flow emitting the requested Match object or null if not found.
     */
    fun getMatch(id: Int): Flow<Match?>

    /**
     * Inserts a new match into the repository.
     *
     * @param match The Match object to be inserted.
     */
    suspend fun insertMatch(match: Match)

    /**
     * Deletes a match from the repository.
     *
     * @param match The Match object to be deleted.
     */
    suspend fun deleteMatch(match: Match)


    /**
     * Updates an existing match in the repository.
     *
     * @param match The updated Match object.
     */
    suspend fun updateMatch(match: Match)

    /**
     * Refreshes the match data for a given date by triggering a background task.
     *
     * @param date The date for which the data should be refreshed.
     */
    suspend fun refresh(date: String)


    /**
     * A flow of WorkInfo representing the status of the Wi-Fi notification background task.
     */
    var wifiWorkInfo: Flow<WorkInfo>

}

/**
 * An implementation of the MatchRepository interface that provides caching and remote data
 * retrieval using WorkManager.
 *
 * @param matchDao The DAO (Data Access Object) for Match entities.
 * @param matchApiService The service for fetching match data from a remote source.
 * @param context The application context.
 */
class CachingMatchRepository(private val matchDao: MatchDao, private val matchApiService: MatchApiService, context: Context) : MatchRepository {

    /**
     * Retrieves all matches for a given date. If no matches are found in the local database,
     * it triggers a remote data refresh using the refresh method.
     *
     * @param date The date for which matches should be retrieved.
     * @return A flow emitting a list of Match objects.
     */
    override fun getMatch(date: String): Flow<List<Match>> {
        return matchDao.getAllItems(date).map {
            it.asDomainMatches()
        }.onEach {
            if (it.isEmpty()) {
                refresh(date)
            }
        }
    }

    /**
     * Retrieves a specific match by its ID.
     *
     * @param id The ID of the match to retrieve.
     * @return A flow emitting the requested Match object or null if not found.
     */
    override fun getMatch(id: Int): Flow<Match?> {
        return matchDao.getItem(id).map {
            it.asDomainMatch()
        }
    }

    /**
     * Inserts a new match into the local database.
     *
     * @param match The Match object to be inserted.
     */
    override suspend fun insertMatch(match: Match) {
        matchDao.insert(match.asDbMatch())
    }

    /**
     * Deletes a match from the local database.
     *
     * @param match The Match object to be deleted.
     */
    override suspend fun deleteMatch(match: Match) {
        matchDao.delete(match.asDbMatch())
    }

    /**
     * Updates an existing match in the local database.
     *
     * @param match The updated Match object.
     */
    override suspend fun updateMatch(match: Match) {
        matchDao.update(match.asDbMatch())
    }

    // Internal ID for tracking the Wi-Fi notification background task.

    private var workID = UUID(1, 2)

    // WorkManager instance for managing background tasks.
    private val workManager = WorkManager.getInstance(context)

    // A flow of WorkInfo representing the status of the Wi-Fi notification background task.
    override var wifiWorkInfo: Flow<WorkInfo> =
        workManager.getWorkInfoByIdFlow(workID)

    /**
     * Refreshes the match data for a given date by scheduling a Wi-Fi dependent background task.
     * This method enqueues a OneTimeWorkRequest for the Wi-FiNotificationWorker and updates
     * the wifiWorkInfo flow to track the status of the background task.
     *
     * @param date The date for which the data should be refreshed.
     */
    override suspend fun refresh(date: String) {

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


        // Use coroutines to perform the actual API request for match data.
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