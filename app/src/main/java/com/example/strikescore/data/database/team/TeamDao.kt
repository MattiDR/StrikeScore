package com.example.strikescore.data.database.team

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy.Companion as OnConflictStrategy1

/**
 * Data Access Object (DAO) interface for the Team entity.
 *
 * This interface defines methods for performing CRUD (Create, Read, Update, Delete) operations
 * on the "teams" table in the Room database.
 */
@Dao
interface TeamDao {
    /**
     * Inserts a [DbTeam] into the database.
     *
     * If there is a conflict with an existing team (based on name), it replaces the existing team.
     *
     * @param item The [DbTeam] to be inserted or replaced.
     */
    @Insert(onConflict = OnConflictStrategy1.REPLACE)
    suspend fun insert(item: DbTeam)

    /**
     * Updates an existing [DbTeam] in the database.
     *
     * @param item The [DbTeam] to be updated.
     */
    @Update
    suspend fun update(item: DbTeam)
    /**
     * Deletes a [DbTeam] from the database.
     *
     * @param item The [DbTeam] to be deleted.
     */
    @Delete
    suspend fun delete(item: DbTeam)
    /**
     * Retrieves a [DbTeam] from the database based on its name.
     *
     * @param name The name of the [DbTeam] to be retrieved.
     * @return A [Flow] emitting the [DbTeam] with the specified name.
     */
    @Query("SELECT * from teams WHERE name = :name")
    fun getItem(name: String): Flow<DbTeam>
    /**
     * Retrieves all [DbTeam] items from the database.
     *
     * Teams are ordered by name in ascending order.
     *
     * @return A [Flow] emitting a list of [DbTeam] items.
     */
    @Query("SELECT * from teams ORDER BY name ASC")
    fun getAllItems(): Flow<List<DbTeam>>
}