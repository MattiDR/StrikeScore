package com.example.strikescore.data.database.standings

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.strikescore.data.database.team.DbTeam
import com.example.strikescore.data.database.team.asDbTeam
import com.example.strikescore.data.database.team.asDomainTeam
import com.example.strikescore.model.Standings

/**
 * Database entity representing standings for a team.
 *
 * This class is annotated with [Entity] to specify that it represents a table in the Room database.
 * The table name is "standings".
 *
 * @property position The position of the team in the standings.
 * @property team An embedded [DbTeam] object representing the team.
 * @property playedGames The number of games played by the team.
 * @property won The number of games won by the team.
 * @property draw The number of games drawn by the team.
 * @property lost The number of games lost by the team.
 * @property points The total points earned by the team.
 * @property goalsFor The number of goals scored by the team.
 * @property goalsAgainst The number of goals conceded by the team.
 * @property goalDifference The goal difference for the team.
 */
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

/**
 * Extension function to convert a [DbStandings] to a [Standings] object.
 *
 * @return Corresponding [Standings] object.
 */
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

/**
 * Extension function to convert a [Standings] object to a [DbStandings].
 *
 * @return Corresponding [DbStandings] object.
 */
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

/**
 * Extension function to convert a list of [DbStandings] objects to a list of [Standings] objects.
 *
 * @return Corresponding list of [Standings] objects.
 */
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