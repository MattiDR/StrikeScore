package com.example.strikescore.ui

import StrikeScoreBottomAppBar
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.strikescore.ui.util.StrikeScoreNavigationType
import androidx.navigation.compose.rememberNavController
import com.example.strikescore.ui.components.StrikeScoreAppAppBar
import com.example.strikescore.ui.navigation.StrikeScoreOverviewScreen
import com.example.strikescore.ui.navigation.navComponent

@Composable
fun StrikeScoreApp(
    navigationType: StrikeScoreNavigationType,
    navController: NavHostController = rememberNavController()
){
    val backStackEntry by navController.currentBackStackEntryAsState()

    val goToMatches = { navController.navigate(StrikeScoreOverviewScreen.Matches.name) {launchSingleTop = true} }
    val goToStandings = { navController.navigate(StrikeScoreOverviewScreen.Standings.name) {launchSingleTop = true} }
    val goToTeams = { navController.navigate(StrikeScoreOverviewScreen.Teams.name) {launchSingleTop = true} }

    val currentScreenTitle = StrikeScoreOverviewScreen.valueOf(
        backStackEntry?.destination?.route ?: StrikeScoreOverviewScreen.Matches.name,
    ).title

    if (navigationType == StrikeScoreNavigationType.PERMANENT_NAVIGATION_DRAWER) {

    }else if(navigationType == StrikeScoreNavigationType.BOTTOM_NAVIGATION) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                StrikeScoreAppAppBar(
                    currentScreenTitle = currentScreenTitle,
                )
            },
            bottomBar = {

                StrikeScoreBottomAppBar(goToTeams,goToMatches , goToStandings)

            }
        ) { innerPadding ->
            navComponent(navController, modifier = Modifier.padding(innerPadding))
        }
    }
}