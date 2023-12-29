package com.example.strikescore.ui

import StrikeScoreBottomAppBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.strikescore.ui.util.StrikeScoreNavigationType
import androidx.navigation.compose.rememberNavController
import com.example.strikescore.R
import com.example.strikescore.ui.components.NavigationDrawerContent
import com.example.strikescore.ui.components.StrikeScoreAppAppBar
import com.example.strikescore.ui.components.StrikeScoreNavigationRail
import com.example.strikescore.ui.matchScreen.MatchOverview
import com.example.strikescore.ui.navigation.StrikeScoreOverviewScreen
import com.example.strikescore.ui.navigation.navComponent

@Composable
fun StrikeScoreApp(
    navigationType: StrikeScoreNavigationType,
    navController: NavHostController = rememberNavController()
){
    val backStackEntry by navController.currentBackStackEntryAsState()

    val goMatch: () -> Unit = {
        navController.popBackStack(
            StrikeScoreOverviewScreen.Matches.name,
            inclusive = false
        )
    }

    val goToMatches = { navController.navigate(StrikeScoreOverviewScreen.Matches.name) {launchSingleTop = true} }
    val goToStandings = { navController.navigate(StrikeScoreOverviewScreen.Standings.name) {launchSingleTop = true} }
    val goToTeams = { navController.navigate(StrikeScoreOverviewScreen.Teams.name) {launchSingleTop = true} }

    val currentScreenTitle = StrikeScoreOverviewScreen.valueOf(
        backStackEntry?.destination?.route ?: StrikeScoreOverviewScreen.Matches.name,
    ).title

    if (navigationType == StrikeScoreNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(drawerContent = {
            PermanentDrawerSheet(Modifier.width(dimensionResource(id = R.dimen.drawer_width))) {
                NavigationDrawerContent(
                    selectedDestination = navController.currentDestination,
                    onTabPressed = { node: String -> navController.navigate(node) },
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                        .padding(dimensionResource(R.dimen.drawer_padding_content)),
                )
                
            }
        }) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    StrikeScoreAppAppBar(
                        currentScreenTitle = currentScreenTitle,
                    )
                },
                // modifier = Modifier.padding(dimensionResource(id = R.dimen.drawer_width), 0.dp, 0.dp, 0.dp )
            ) { innerPadding ->

                navComponent(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }

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
    }else{
        Row {
            AnimatedVisibility(visible = navigationType == StrikeScoreNavigationType.NAVIGATION_RAIL) {
                val navigationRailContentDescription = stringResource(R.string.navigation_rail)
                StrikeScoreNavigationRail(
                    selectedDestination = navController.currentDestination,
                    onTabPressed = { node: String -> navController.navigate(node) },
                )
            }
            Scaffold(
                containerColor = Color.Transparent,
            ) { innerPadding ->
                navComponent(navController, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}