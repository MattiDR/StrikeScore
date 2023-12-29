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
                        tint = Color.White,
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
