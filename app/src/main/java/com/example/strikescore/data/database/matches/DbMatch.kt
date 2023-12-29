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

/**
 * Database entity representing a match.
 *
 * This class is annotated with [Entity] to specify that it represents a table in the Room database.
 * The table name is "matches". It uses the [TypeConverters] annotation to specify the [ScoreTypeConverter]
 * for converting the [Score] object to a format that can be stored in the database.
 *
 * @property id Primary key for the match.
 * @property homeTeam Embedded [DbTeam] object representing the home team.
 * @property awayTeam Embedded [DbTeam] object representing the away team.
 * @property status Status of the match.
 * @property matchday Matchday of the match.
 * @property score [Score] object representing the match score.
 * @property utcDate UTC date and time of the match.
 */
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

/**
 * Extension function to convert a [Match] object to a [DbMatch].
 *
 * @return Corresponding [DbMatch] object.
 */
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

/**
 * Extension function to convert a list of [DbMatch] objects to a list of [Match] objects.
 *
 * @return Corresponding list of [Match] objects.
 */
fun List<DbMatch>.asDomainMatches(): List<Match> {
    var matchList = this.map {
        Match(it.id, it.homeTeam.asDomainTeam(), it.awayTeam.asDomainTeam(), it.status, it.matchday, it.utcDate, it.score)
    }
    return matchList
}
