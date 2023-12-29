package com.example.strikescore.model

/**
 * Represents a team in the StrikeScore application.
 *
 * @property name The name of the team.
 * @property tla The three-letter acronym representing the team.
 * @property crest The URL or path to the team's crest or logo.
 */
data class Team (
    val name: String,
    val tla: String,
    val crest: String,
)

