package com.example.strikescore.fake

import com.example.strikescore.data.database.matches.DbMatch
import com.example.strikescore.model.FullTime
import com.example.strikescore.model.Match
import com.example.strikescore.model.Score

object FakeMatchDataSource {
    val matches = listOf(
        Match(
            1,
            FakeTeamDataSource.teams[0],
            FakeTeamDataSource.teams[1],
            "FINISHED",
            1,
            "2021-08-14T14:00:00Z",
            Score(FullTime(2, 0)),
        ),
        Match(
        2,
            FakeTeamDataSource.teams[1],
            FakeTeamDataSource.teams[0],
            "FINISHED",
            2,
            "2021-08-21T14:00:00Z",
            Score(FullTime(0, 1)),
        ),
        Match(
            3,
            FakeTeamDataSource.teams[0],
            FakeTeamDataSource.teams[1],
            "FINISHED",
            3,
            "2021-08-14T14:00:00Z",
            Score(FullTime(2, 2)),
        ),

    )
}