package com.example.strikescore.data.database.team

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.strikescore.model.Team

@Entity(tableName = "teams")
data class DbTeam(
    @PrimaryKey
    val name: String,
    val tla: String,
    val crest: String,

    )

fun DbTeam.asDomainTeam(): Team {
    return Team(
        this.name,
        this.tla,
        this.crest,
    )
}

fun Team.asDbTeam(): DbTeam {
    return DbTeam(
        name = this.name,
        tla = this.tla,
        crest = this.crest,
    )
}

fun List<DbTeam>.asDomainTeams(): List<Team> {
    var teamList = this.map {
        Team(it.name, it.tla, it.crest)
    }
    return teamList
}