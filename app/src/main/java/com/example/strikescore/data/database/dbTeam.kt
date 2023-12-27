package com.example.strikescore.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.strikescore.model.Team

@Entity(tableName = "teams")
data class dbTeam(
    @PrimaryKey
    val name: String,
    val tla: String,
    val crest: String,
    val address: String,
    val website: String,
    val clubColors: String,
    val venue: String,

    )

fun dbTeam.asDomainTeam(): Team {
    return Team(
        this.name,
        this.tla,
        this.crest,
        this.address,
        this.website,
        this.clubColors,
        this.venue,
    )
}

fun Team.asDbTeam(): dbTeam {
    return dbTeam(
        name = this.name,
        tla = this.tla,
        crest = this.crest,
        address = this.address,
        website = this.website,
        clubColors = this.clubColors,
        venue = this.venue,
    )
}

fun List<dbTeam>.asDomainTeams(): List<Team> {
    var teamList = this.map {
        Team(it.name, it.tla, it.crest, it.address, it.website, it.clubColors, it.venue)
    }
    return teamList
}