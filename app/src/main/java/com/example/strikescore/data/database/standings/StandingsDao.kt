package com.example.strikescore.data.database.standings

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StandingsDao {
    /**
     * Inserts a [DbStandings] into the database.
     *
     * If there is a conflict with an existing standings (based on position), it replaces the existing standings.
     *
     * @param item The [DbStandings] to be inserted or replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: DbStandings)

    /**
     * Updates an existing [DbStandings] in the database.
     *
     * @param item The [DbStandings] to be updated.
     */
    @Update
    suspend fun update(item: DbStandings)

    /**
     * Deletes a [DbStandings] from the database.
     *
     * @param item The [DbStandings] to be deleted.
     */
    @Delete
    suspend fun delete(item: DbStandings)
    /**
     * Retrieves a [DbStandings] from the database based on its position.
     *
     * @param position The position of the [DbStandings] to be retrieved.
     * @return A [Flow] emitting the [DbStandings] with the specified position.
     */
    @Query("SELECT * from standings WHERE position = :position")
    fun getItem(position: Int): Flow<DbStandings>
    /**
     * Retrieves all [DbStandings] items from the database.
     *
     * Standings are ordered by position in ascending order.
     *
     * @return A [Flow] emitting a list of [DbStandings] items.
     */
    @Query("SELECT * from standings ORDER BY position ASC")
    fun getAllItems(): Flow<List<DbStandings>>
}