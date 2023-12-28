package com.example.strikescore.data.database

import com.example.strikescore.data.database.matches.ScoreTypeConverter
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.strikescore.data.database.matches.MatchDao
import com.example.strikescore.data.database.matches.dbMatch
import com.example.strikescore.data.database.standings.StandingsDao
import com.example.strikescore.data.database.standings.dbStandings
import com.example.strikescore.data.database.team.TeamDao
import com.example.strikescore.data.database.team.dbTeam

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [dbTeam::class, dbStandings::class, dbMatch::class], version = 19, exportSchema = false)
@TypeConverters(ScoreTypeConverter::class)
abstract class StrikeScoreDb : RoomDatabase() {

    abstract fun teamDao(): TeamDao

    abstract fun standingsDao(): StandingsDao

    abstract fun matchDao(): MatchDao

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