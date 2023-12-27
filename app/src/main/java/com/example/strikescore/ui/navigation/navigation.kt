package com.example.strikescore.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.strikescore.ui.matchScreen.MatchScreen
import com.example.strikescore.ui.standingsScreen.StandingsOverview
import com.example.strikescore.ui.teamScreen.TeamOverview

@Composable
fun navComponent(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = StrikeScoreOverviewScreen.Teams.name,
        modifier = modifier,
    ) {
        composable(route = StrikeScoreOverviewScreen.Teams.name) {
            TeamOverview()
        }
        composable(route = StrikeScoreOverviewScreen.Matches.name) {
            MatchScreen()
        }
        composable(route = StrikeScoreOverviewScreen.Standings.name) {
            StandingsOverview()
        }
    }
}