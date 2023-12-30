package com.example.strikescore.ui

import StrikeScoreBottomAppBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.strikescore.ui.util.StrikeScoreNavigationType
import androidx.navigation.compose.rememberNavController
import com.example.strikescore.R
import com.example.strikescore.ui.components.NavigationDrawerContent
import com.example.strikescore.ui.components.StrikeScoreAppAppBar
import com.example.strikescore.ui.components.StrikeScoreNavigationRail
import com.example.strikescore.ui.navigation.StrikeScoreOverviewScreen
import com.example.strikescore.ui.navigation.NavComponent

/**
 * Composable function representing the main structure of the Strike Score application.
 *
 * This function sets up the UI layout based on the provided [navigationType].
 *
 * @param navigationType The type of navigation used in the application, such as
 * [StrikeScoreNavigationType.BOTTOM_NAVIGATION], [StrikeScoreNavigationType.NAVIGATION_RAIL],
 * or [StrikeScoreNavigationType.PERMANENT_NAVIGATION_DRAWER].
 * @param navController The navigation controller for managing navigation within the app.
 */
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
                containerColor = MaterialTheme.colorScheme.background,
                topBar = {
                    StrikeScoreAppAppBar(
                        currentScreenTitle = currentScreenTitle,
                    )
                },
                // modifier = Modifier.padding(dimensionResource(id = R.dimen.drawer_width), 0.dp, 0.dp, 0.dp )
            ) { innerPadding ->

                NavComponent(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }

    }else if(navigationType == StrikeScoreNavigationType.BOTTOM_NAVIGATION) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                StrikeScoreAppAppBar(
                    currentScreenTitle = currentScreenTitle,
                )
            },
            bottomBar = {
                StrikeScoreBottomAppBar(goToTeams,goToMatches , goToStandings)
            }
        ) { innerPadding ->
            NavComponent(navController, modifier = Modifier.padding(innerPadding))
        }
    }else{
        Row {
            AnimatedVisibility(visible = navigationType == StrikeScoreNavigationType.NAVIGATION_RAIL) {
                StrikeScoreNavigationRail(
                    selectedDestination = navController.currentDestination,
                    onTabPressed = { node: String -> navController.navigate(node) },
                )
            }
            Scaffold(
                containerColor = Color.Transparent,
            ) { innerPadding ->
                NavComponent(navController, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}