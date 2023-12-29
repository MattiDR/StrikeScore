package com.example.strikescore.data.database.matches

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: DbMatch)

    @Update
    suspend fun update(item: DbMatch)

    @Delete
    suspend fun delete(item: DbMatch)

    @Query("SELECT * from matches WHERE id = :id")
    fun getItem(id: Int): Flow<DbMatch>

    @Query("SELECT * FROM matches WHERE date(utcDate) = date(:utcDate) ORDER BY utcDate ASC")
    fun getAllItems(utcDate: String): Flow<List<DbMatch>>
}