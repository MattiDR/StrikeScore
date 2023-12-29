package com.example.strikescore.data.database.matches

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.strikescore.data.database.team.DbTeam
import com.example.strikescore.data.database.team.asDbTeam
import com.example.strikescore.data.database.team.asDomainTeam
import com.example.strikescore.model.Match
import com.example.strikescore.model.Score

@Entity(tableName = "matches")
@TypeConverters(ScoreTypeConverter::class)
data class DbMatch (
    @PrimaryKey
    val id: Int,
    @Embedded(prefix = "home_")
    val homeTeam: DbTeam,
    @Embedded(prefix = "away_")
    val awayTeam: DbTeam,
    val status: String,
    val matchday: Int,
    val score : Score,
    val utcDate: String,
)

/**
 * Extension function to convert a [DbMatch] to a [Match] object.
 *
 * @return Corresponding [Match] object.
 */
fun DbMatch.asDomainMatch(): Match {
    return Match(
        this.id,
        this.homeTeam.asDomainTeam(),
        this.awayTeam.asDomainTeam(),
        this.status,
        this.matchday,
        this.utcDate,
        this.score,
    )
}

fun Match.asDbMatch(): DbMatch {
    return DbMatch(
        id = this.id,
        homeTeam = this.homeTeam.asDbTeam(),
        awayTeam = this.awayTeam.asDbTeam(),
        status = this.status,
        matchday = this.matchday,
        utcDate = this.utcDate,
        score = this.score,
    )
}

fun List<DbMatch>.asDomainMatches(): List<Match> {
    var matchList = this.map {
        Match(it.id, it.homeTeam.asDomainTeam(), it.awayTeam.asDomainTeam(), it.status, it.matchday, it.utcDate, it.score)
    }
    return matchList
}
