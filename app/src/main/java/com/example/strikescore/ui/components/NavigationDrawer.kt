package com.example.strikescore.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import com.example.strikescore.R
import com.example.strikescore.ui.navigation.StrikeScoreOverviewScreen

/**
 * Composable function representing the content of the navigation drawer, displaying
 * items for each destination in the app.
 *
 * @param selectedDestination The currently selected navigation destination.
 * @param onTabPressed Callback function to be invoked when a navigation item is pressed.
 * @param modifier Modifier for styling the [NavigationDrawerContent] composable.
 */
@Composable
fun NavigationDrawerContent(
    selectedDestination: NavDestination?,
    onTabPressed: ((String) -> Unit),
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        for (navItem in StrikeScoreOverviewScreen.values()) {
            val painter: Painter = painterResource(id = navItem.icon)
            NavigationDrawerItem(
                selected = selectedDestination?.route == navItem.name,
                label = {
                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = navItem.name,
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.drawer_padding_header)),
                    )
                },
                icon = {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimary,
                        painter = painter,
                        contentDescription = navItem.name,
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                ),
                onClick = { onTabPressed(navItem.name) },
            )
        }
    }
}
