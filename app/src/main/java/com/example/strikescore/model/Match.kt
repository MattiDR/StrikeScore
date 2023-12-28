package com.example.strikescore.model

import java.util.Date

data class Match(
    val id: Int,
    val homeTeam: Team,
    val awayTeam: Team,
    val status: String,
    val matchday: Int,
    val utcDate: String,
    val score: Score,
)

data class Score(
    val fullTime: FullTime,
)

data class FullTime(
    val home: Int?,
    val away: Int?,
)