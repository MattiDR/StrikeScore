package com.example.strikescore.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.strikescore.data.database.standings.StandingsDao
import com.example.strikescore.data.database.standings.dbStandings
import com.example.strikescore.data.database.team.TeamDao
import com.example.strikescore.data.database.team.dbTeam

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [dbTeam::class, dbStandings::class], version = 5, exportSchema = false)
abstract class StrikeScoreDb : RoomDatabase() {

    abstract fun teamDao(): TeamDao

    abstract fun standingsDao(): StandingsDao

    companion object {
        @Volatile
        private var Instance: StrikeScoreDb? = null

        fun getDatabase(context: Context): StrikeScoreDb {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, StrikeScoreDb::class.java, "StrikeScore_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}