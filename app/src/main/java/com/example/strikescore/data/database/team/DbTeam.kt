package com.example.strikescore.data.database.team

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.strikescore.model.Team

/**
 * Database entity representing a team.
 *
 * This class is annotated with [Entity] to specify that it represents a table in the Room database.
 * The table name is "teams".
 *
 * @property name The name of the team.
 * @property tla The three-letter acronym of the team.
 * @property crest The URL of the team's crest.
 */
@Entity(tableName = "teams")
data class DbTeam(
    @PrimaryKey
    val name: String,
    val tla: String,
    val crest: String,

    )
/**
 * Extension function to convert a [DbTeam] to a [Team] object.
 *
 * @return Corresponding [Team] object.
 */
fun DbTeam.asDomainTeam(): Team {
    return Team(
        this.name,
        this.tla,
        this.crest,
    )
}
/**
 * Extension function to convert a [Team] object to a [DbTeam].
 *
 * @return Corresponding [DbTeam] object.
 */
fun Team.asDbTeam(): DbTeam {
    return DbTeam(
        name = this.name,
        tla = this.tla,
        crest = this.crest,
    )
}
/**
 * Extension function to convert a list of [DbTeam] objects to a list of [Team] objects.
 *
 * @return Corresponding list of [Team] objects.
 */
fun List<DbTeam>.asDomainTeams(): List<Team> {
    var teamList = this.map {
        Team(it.name, it.tla, it.crest)
    }
    return teamList
}