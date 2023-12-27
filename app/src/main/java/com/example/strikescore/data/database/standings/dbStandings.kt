package com.example.strikescore.data.database.standings

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.strikescore.data.database.team.asDbTeam
import com.example.strikescore.data.database.team.asDomainTeam
import com.example.strikescore.data.database.team.dbTeam
import com.example.strikescore.model.Standings
import com.example.strikescore.model.Team

@Entity(tableName = "standings")
data class dbStandings(
    @PrimaryKey
    val position: Int,
    @Embedded
    val team: dbTeam,
    val playedGames: Int,
    val won: Int,
    val draw: Int,
    val lost: Int,
    val points: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int,
)

fun dbStandings.asDomainStandings(): Standings {
    return Standings(
        this.position,
        this.team.asDomainTeam(),
        this.playedGames,
        this.won,
        this.draw,
        this.lost,
        this.points,
        this.goalsFor,
        this.goalsAgainst,
        this.goalDifference,
    )
}

fun Standings.asDbStandings(): dbStandings {
    return dbStandings(
        position = this.position,
        team = this.team.asDbTeam(),
        playedGames = this.playedGames,
        won = this.won,
        draw = this.draw,
        lost = this.lost,
        points = this.points,
        goalsFor = this.goalsFor,
        goalsAgainst = this.goalsAgainst,
        goalDifference = this.goalDifference,
    )
}

fun List<dbStandings>.asDomainStandings(): List<Standings> {
    var standingsList = this.map {
        Standings(
            it.position,
            it.team.asDomainTeam(),
            it.playedGames,
            it.won,
            it.draw,
            it.lost,
            it.points,
            it.goalsFor,
            it.goalsAgainst,
            it.goalDifference,
        )
    }
    return standingsList
}