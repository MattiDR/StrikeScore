package com.example.strikescore.fake

import com.example.strikescore.model.Team

object FakeTeamDataSource {

    val teams = listOf(
        Team(
            "Arsenal FC",
            "ARS",
            "https://crests.football-data.org/1.svg",
        ),
        Team(
            "Aston Villa FC",
            "AVL",
            "https://crests.football-data.org/2.svg",
        ),
    )
}