package com.example.strikescore.data.database.team

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy.Companion as OnConflictStrategy1

@Dao
interface TeamDao {
    @Insert(onConflict = OnConflictStrategy1.REPLACE)
    suspend fun insert(item: dbTeam)

    @Update
    suspend fun update(item: dbTeam)

    @Delete
    suspend fun delete(item: dbTeam)

    @Query("SELECT * from teams WHERE name = :name")
    fun getItem(name: String): Flow<dbTeam>

    @Query("SELECT * from teams ORDER BY name ASC")
    fun getAllItems(): Flow<List<dbTeam>>
}