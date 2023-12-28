package com.example.strikescore.data.database.matches

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.strikescore.data.database.team.asDbTeam
import com.example.strikescore.data.database.team.asDomainTeam
import com.example.strikescore.data.database.team.dbTeam
import com.example.strikescore.model.Match
import java.util.Date


@Entity(tableName = "matches")
data class dbMatch (
    @PrimaryKey
    val id: Int,
    @Embedded(prefix = "home_")
    val homeTeam: dbTeam,
    @Embedded(prefix = "away_")
    val awayTeam: dbTeam,
    val status: String,
    val matchday: Int,
//    val score : ,
    val utcDate: String,
)

fun dbMatch.asDomainMatch(): Match {
    return Match(
        this.id,
        this.homeTeam.asDomainTeam(),
        this.awayTeam.asDomainTeam(),
        this.status,
        this.matchday,
        this.utcDate,
    )
}

fun Match.asDbMatch(): dbMatch {
    return dbMatch(
        id = this.id,
        homeTeam = this.homeTeam.asDbTeam(),
        awayTeam = this.awayTeam.asDbTeam(),
        status = this.status,
        matchday = this.matchday,
        utcDate = this.utcDate,
    )
}

fun List<dbMatch>.asDomainMatches(): List<Match> {
    var matchList = this.map {
        Match(it.id, it.homeTeam.asDomainTeam(), it.awayTeam.asDomainTeam(), it.status, it.matchday, it.utcDate)
    }
    return matchList
}
