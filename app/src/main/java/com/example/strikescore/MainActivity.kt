package com.example.strikescore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.strikescore.ui.theme.StrikeScoreTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.strikescore.ui.util.StrikeScoreNavigationType
import com.example.strikescore.ui.StrikeScoreApp
import com.example.strikescore.ui.theme.backgroundTeamItem

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean = false,
    val badgeCount: Int? = null,
)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StrikeScoreTheme {
                Surface(
                    color = backgroundTeamItem,
                ){
                    val windowSize = calculateWindowSizeClass(activity = this)
                    when (windowSize.widthSizeClass){
                        WindowWidthSizeClass.Compact -> {
                            StrikeScoreApp(StrikeScoreNavigationType.BOTTOM_NAVIGATION)
                        }
                        WindowWidthSizeClass.Medium -> {
                            StrikeScoreApp(StrikeScoreNavigationType.NAVIGATION_RAIL)
                        }
                        WindowWidthSizeClass.Expanded -> {
                            StrikeScoreApp(navigationType = StrikeScoreNavigationType.PERMANENT_NAVIGATION_DRAWER)
                        }
                        else -> {
                            StrikeScoreApp(navigationType = StrikeScoreNavigationType.BOTTOM_NAVIGATION)
                        }
                    }
                }
            }
        }
    }
}
