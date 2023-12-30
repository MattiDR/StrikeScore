package com.example.strikescore.fake

import com.example.strikescore.model.Standings
import com.example.strikescore.model.Team

object FakeStandingDataSource {
    val standings = listOf(
        Standings(
            1,
            Team(
                "Arsenal FC",
                "ARS",
                "https://crests.football-data.org/1.svg",
            ),
            10,
            10,
            0,
            0,
            30,
            10,
            0,
            10,
        ),
        Standings(
            2,
            Team(
                "Aston Villa FC",
                "AVL",
                "https://crests.football-data.org/2.svg",
                ),
            10,
            0,
            0,
            10,
            0,
            0,
            10,
            -10,
        ),
    )
}