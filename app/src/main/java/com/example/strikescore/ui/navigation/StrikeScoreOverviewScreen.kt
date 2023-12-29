package com.example.strikescore.ui.navigation

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
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
