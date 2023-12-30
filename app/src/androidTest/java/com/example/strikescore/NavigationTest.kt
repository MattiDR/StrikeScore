package com.example.strikescore

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.strikescore.ui.StrikeScoreApp
import com.example.strikescore.ui.util.StrikeScoreNavigationType
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            StrikeScoreApp(navController = navController, navigationType = StrikeScoreNavigationType.BOTTOM_NAVIGATION)
        }
    }
    @Test
    fun verifyStartDestination() {
        composeTestRule
            .onAllNodesWithText("Teams")[0]
            .assertIsDisplayed()
    }

    @Test
    fun navigateToMatches() {
        composeTestRule
            .onNodeWithContentDescription("navigate to Matches", ignoreCase = true, useUnmergedTree = true)
            .performClick()
        composeTestRule
            .onAllNodesWithText("Matches")[0]
            .assertIsDisplayed()
    }

    @Test
    fun navigateToStandings() {
        composeTestRule
            .onNodeWithContentDescription("navigate to Standings", ignoreCase = true, useUnmergedTree = true)
            .performClick()
        composeTestRule
            .onAllNodesWithText("Standings")[0]
            .assertIsDisplayed()
    }
}