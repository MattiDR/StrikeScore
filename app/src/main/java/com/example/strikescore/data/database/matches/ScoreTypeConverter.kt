package com.example.strikescore.data.database.matches

import androidx.room.TypeConverter
import com.example.strikescore.model.FullTime
import com.example.strikescore.model.Score

class ScoreTypeConverter {
    @TypeConverter
    fun fromScore(score: Score): String {
        if(score.fullTime.home == null || score.fullTime.away == null)
            return "null"
        return "${score.fullTime.home}-${score.fullTime.away}"
    }

    @TypeConverter
    fun toScore(scoreString: String): Score {
        if(scoreString == "null")
            return Score(FullTime(null, null))
        val parts = scoreString.split("-")
        val fullTime = FullTime(parts[0].toInt(), parts[1].toInt())
        return Score(fullTime)
    }
}