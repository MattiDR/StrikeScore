package com.example.strikescore.data.database.matches

import androidx.room.TypeConverter
import com.example.strikescore.model.FullTime
import com.example.strikescore.model.Score

/**
 * Type converter for converting [Score] objects to and from a format that can be stored in the database.
 */
class ScoreTypeConverter {
    /**
     * Converts a [Score] object to its string representation for database storage.
     *
     * @param score The [Score] object to be converted.
     * @return The string representation of the score, or "null" if any part is null.
     */
    @TypeConverter
    fun fromScore(score: Score): String {
        if(score.fullTime.home == null || score.fullTime.away == null)
            return "null"
        return "${score.fullTime.home}-${score.fullTime.away}"
    }

    /**
     * Converts a string representation of a score from the database to a [Score] object.
     *
     * @param scoreString The string representation of the score.
     * @return The corresponding [Score] object.
     */
    @TypeConverter
    fun toScore(scoreString: String): Score {
        if(scoreString == "null")
            return Score(FullTime(null, null))
        val parts = scoreString.split("-")
        val fullTime = FullTime(parts[0].toInt(), parts[1].toInt())
        return Score(fullTime)
    }
}