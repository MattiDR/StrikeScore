package com.example.strikescore.ui.navigation

import androidx.annotation.StringRes
import com.example.strikescore.R

/**
 * Enumeration defining screens in the StrikeScore app with associated titles and icons.
 *
 * @property title String resource ID representing the title of the screen.
 * @property icon Drawable resource ID representing the icon for the screen.
 */
enum class StrikeScoreOverviewScreen(@StringRes val title: Int, val icon: Int) {
    Teams(title = R.string.team_title, icon = R.drawable.teams),
    Matches(title = R.string.matches_title, icon = R.drawable.ball),
    Standings(title= R.string.standings_title, icon = R.drawable.standings)
}
