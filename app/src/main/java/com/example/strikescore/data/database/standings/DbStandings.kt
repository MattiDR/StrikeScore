package com.example.strikescore.data.database.standings

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.strikescore.data.database.team.DbTeam
import com.example.strikescore.data.database.team.asDbTeam
import com.example.strikescore.data.database.team.asDomainTeam
import com.example.strikescore.model.Standings

@Entity(tableName = "standings")
data class DbStandings(
    @PrimaryKey
    val position: Int,
    @Embedded
    val team: DbTeam,
    val playedGames: Int,
    val won: Int,
    val draw: Int,
    val lost: Int,
    val points: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int,
)

fun DbStandings.asDomainStandings(): Standings {
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

fun Standings.asDbStandings(): DbStandings {
    return DbStandings(
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

fun List<DbStandings>.asDomainStandings(): List<Standings> {
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