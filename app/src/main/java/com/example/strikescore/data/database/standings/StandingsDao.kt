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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: DbStandings)

    @Update
    suspend fun update(item: DbStandings)

    @Delete
    suspend fun delete(item: DbStandings)

    @Query("SELECT * from standings WHERE position = :position")
    fun getItem(position: Int): Flow<DbStandings>

    @Query("SELECT * from standings ORDER BY position ASC")
    fun getAllItems(): Flow<List<DbStandings>>
}