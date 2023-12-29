package com.example.strikescore.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import com.example.strikescore.ui.navigation.StrikeScoreOverviewScreen
import com.example.strikescore.ui.theme.backgroundBottomNavigationBar
import com.example.strikescore.ui.theme.backgroundSelectedBottomNavigationBar

@Composable
fun StrikeScoreNavigationRail(selectedDestination: NavDestination?, onTabPressed: (String) -> Unit, modifier: Modifier = Modifier) {
    NavigationRail(modifier = modifier, containerColor = backgroundBottomNavigationBar) {
        for (navItem in StrikeScoreOverviewScreen.values()) {
            val painter: Painter = painterResource(id = navItem.icon)
            NavigationRailItem(
                colors = NavigationRailItemDefaults.colors(
                    indicatorColor = backgroundSelectedBottomNavigationBar
                ),
                selected = selectedDestination?.route == navItem.name ,
                onClick = { onTabPressed(navItem.name) },
                icon = {
                    Icon(
                        tint = Color.White,
                        painter = painter,
                        contentDescription = navItem.name,
                    )
                },
            )
        }
    }
}