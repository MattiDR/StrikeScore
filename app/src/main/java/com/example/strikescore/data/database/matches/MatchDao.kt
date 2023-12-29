package com.example.strikescore.data.database.matches

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) interface for the Match entity.
 *
 * This interface defines methods for performing CRUD (Create, Read, Update, Delete) operations
 * on the "matches" table in the Room database.
 */
@Dao
interface MatchDao {
    /**
     * Inserts a [DbMatch] into the database.
     *
     * If there is a conflict with an existing match (based on primary key), it replaces the existing match.
     *
     * @param item The [DbMatch] to be inserted or replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: DbMatch)

    /**
     * Updates an existing [DbMatch] in the database.
     *
     * @param item The [DbMatch] to be updated.
     */
    @Update
    suspend fun update(item: DbMatch)

    /**
     * Deletes a [DbMatch] from the database.
     *
     * @param item The [DbMatch] to be deleted.
     */
    @Delete
    suspend fun delete(item: DbMatch)

    /**
     * Retrieves a [DbMatch] from the database based on its ID.
     *
     * @param id The ID of the [DbMatch] to be retrieved.
     * @return A [Flow] emitting the [DbMatch] with the specified ID.
     */
    @Query("SELECT * from matches WHERE id = :id")
    fun getItem(id: Int): Flow<DbMatch>

    /**
     * Retrieves all [DbMatch] items from the database for a specific date.
     *
     * Matches are ordered by UTC date in ascending order.
     *
     * @param utcDate The UTC date in the format "yyyy-MM-dd" for which matches should be retrieved.
     * @return A [Flow] emitting a list of [DbMatch] items for the specified date.
     */
    @Query("SELECT * FROM matches WHERE date(utcDate) = date(:utcDate) ORDER BY utcDate ASC")
    fun getAllItems(utcDate: String): Flow<List<DbMatch>>
}