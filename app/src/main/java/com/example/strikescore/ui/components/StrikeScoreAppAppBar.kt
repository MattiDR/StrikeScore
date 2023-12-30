package com.example.strikescore.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Composable function representing the top app bar of the StrikeScore app.
 *
 * @param currentScreenTitle The resource ID for the title of the current screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrikeScoreAppAppBar(
    currentScreenTitle: Int,
) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),

        title = {
            Text(stringResource(id = currentScreenTitle))
        },
    )
}