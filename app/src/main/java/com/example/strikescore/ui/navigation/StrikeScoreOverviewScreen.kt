package com.example.strikescore.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.strikescore.R


enum class StrikeScoreOverviewScreen(@StringRes val title: Int, val icon: ImageVector) {
    Matches(title = R.string.matches_title, Icons.Filled.DateRange),
    Teams(title = R.string.team_title, icon = Icons.Filled.Info),
    Standings(title= R.string.standings_title, icon = Icons.Filled.List)
}
