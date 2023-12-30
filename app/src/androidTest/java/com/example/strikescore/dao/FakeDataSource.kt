package com.example.strikescore.dao

import com.example.strikescore.model.FullTime
import com.example.strikescore.model.Match
import com.example.strikescore.model.Score
import com.example.strikescore.model.Standings
import com.example.strikescore.model.Team

object FakeDataSource {

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

    val matches = listOf(
        Match(
            1,
            teams[0],
            teams[1],
            "FINISHED",
            1,
            "2021-08-14T14:00:00Z",
            Score(FullTime(2, 0)),
        ),
        Match(
            2,
            teams[1],
            teams[0],
            "FINISHED",
            2,
            "2021-08-21T14:00:00Z",
            Score(FullTime(0, 1)),
        ),
        Match(
            3,
            teams[0],
            teams[1],
            "FINISHED",
            3,
            "2021-08-14T14:00:00Z",
            Score(FullTime(2, 2)),
        ),

        )


    val standings = listOf(
        Standings(
            1,
            teams[0],
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
            teams[1],
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