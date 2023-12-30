package com.example.strikescore.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination
import com.example.strikescore.ui.navigation.StrikeScoreOverviewScreen

/**
 * Composable function representing the navigation rail in the app, displaying items for each destination.
 *
 * @param selectedDestination The currently selected navigation destination.
 * @param onTabPressed Callback function to be invoked when a navigation item is pressed.
 * @param modifier Modifier for styling the [StrikeScoreNavigationRail] composable.
 */
@Composable
fun StrikeScoreNavigationRail(selectedDestination: NavDestination?, onTabPressed: (String) -> Unit, modifier: Modifier = Modifier) {
    NavigationRail(modifier = modifier, containerColor = MaterialTheme.colorScheme.primary) {
        for (navItem in StrikeScoreOverviewScreen.values()) {
            NavigationRailItem(
                colors = NavigationRailItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.tertiary,
                ),
                selected = selectedDestination?.route == navItem.name ,
                onClick = { onTabPressed(navItem.name) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(navItem.icon),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = navItem.name,
                    )
                },
            )
        }
    }
}