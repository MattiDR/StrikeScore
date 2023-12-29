package com.example.strikescore.model

/**
 * Represents the standings of a team in the StrikeScore application.
 *
 * @property position The current position of the team in the standings.
 * @property team The team associated with the standings.
 * @property playedGames The total number of games played by the team.
 * @property won The number of games won by the team.
 * @property draw The number of games drawn by the team.
 * @property lost The number of games lost by the team.
 * @property points The total points accumulated by the team.
 * @property goalsFor The total number of goals scored by the team.
 * @property goalsAgainst The total number of goals conceded by the team.
 * @property goalDifference The goal difference (goals scored minus goals conceded) for the team.
 */
data class Standings(
    val position: Int,
    val team: Team,
    val playedGames: Int,
    val won: Int,
    val draw: Int,
    val lost: Int,
    val points: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int,
)
