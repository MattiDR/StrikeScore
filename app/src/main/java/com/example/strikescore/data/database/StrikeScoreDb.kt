package com.example.strikescore.data.database

import com.example.strikescore.data.database.matches.ScoreTypeConverter
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.strikescore.data.database.matches.MatchDao
import com.example.strikescore.data.database.matches.DbMatch
import com.example.strikescore.data.database.standings.StandingsDao
import com.example.strikescore.data.database.standings.DbStandings
import com.example.strikescore.data.database.team.TeamDao
import com.example.strikescore.data.database.team.DbTeam

/**
 * Room database class for the StrikeScore app.
 *
 * This class is annotated with [Database] to specify the entities it contains and the database version.
 * It extends [RoomDatabase] to provide access to DAOs.
 * The [TypeConverters] annotation is used to specify the [ScoreTypeConverter] for type conversion.
 */
@Database(entities = [DbTeam::class, DbStandings::class, DbMatch::class], version = 19, exportSchema = false)
@TypeConverters(ScoreTypeConverter::class)
abstract class StrikeScoreDb : RoomDatabase() {

    /**
     * Abstract method to get access to the [TeamDao] for team-related database operations.
     */
    abstract fun teamDao(): TeamDao

    /**
     * Abstract method to get access to the [StandingsDao] for standings-related database operations.
     */
    abstract fun standingsDao(): StandingsDao

    /**
     * Abstract method to get access to the [MatchDao] for match-related database operations.
     */
    abstract fun matchDao(): MatchDao

    /**
     * Companion object holding a volatile reference to the database instance.
     */
    companion object {
        @Volatile
        private var Instance: StrikeScoreDb? = null

        /**
         * Function to get a singleton instance of the [StrikeScoreDb].
         *
         * @param context The application context.
         * @return The singleton instance of [StrikeScoreDb].
         */
        fun getDatabase(context: Context): StrikeScoreDb {
            // If the Instance is not null, return it. Otherwise, create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, StrikeScoreDb::class.java, "StrikeScore_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}