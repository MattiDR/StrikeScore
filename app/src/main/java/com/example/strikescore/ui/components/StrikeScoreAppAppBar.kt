package com.example.strikescore.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.strikescore.ui.theme.backgroundTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrikeScoreAppAppBar(
    currentScreenTitle: Int,
) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = backgroundTopBar,
            titleContentColor = Color.White,
        ),

        title = {
            Text(stringResource(id = currentScreenTitle))
        },
    )
}