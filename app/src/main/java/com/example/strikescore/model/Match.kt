package com.example.strikescore.model

import java.util.Date

/**
 * Represents a Match in the StrikeScore application.
 *
 * @property id The unique identifier for the match.
 * @property homeTeam The home team participating in the match.
 * @property awayTeam The away team participating in the match.
 * @property status The current status of the match (e.g., "scheduled", "in progress", "finished").
 * @property matchday The matchday number for the match.
 * @property utcDate The UTC date and time when the match is scheduled to take place.
 * @property score The scoring details for the match.
 */
data class Match(
    val id: Int,
    val homeTeam: Team,
    val awayTeam: Team,
    val status: String,
    val matchday: Int,
    val utcDate: String,
    val score: Score,
)

/**
 * Represents the scoring details for a match in the StrikeScore application.
 *
 * @property fullTime The full-time scoring details for the match.
 */
data class Score(
    val fullTime: FullTime,
)

/**
 * Represents the full-time scoring details for a match in the StrikeScore application.
 *
 * @property home The number of goals scored by the home team (nullable).
 * @property away The number of goals scored by the away team (nullable).
 */
data class FullTime(
    val home: Int?,
    val away: Int?,
)