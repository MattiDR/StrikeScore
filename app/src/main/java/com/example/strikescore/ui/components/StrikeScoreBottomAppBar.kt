
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.strikescore.R
import com.example.strikescore.ui.theme.StrikeScoreTheme

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
fun StrikeScoreBottomAppBar(goTeams: () -> Unit, goMatches: () -> Unit, goStandings: () -> Unit) {
    StrikeScoreTheme {
        val items = listOf(
            BottomNavigationItem(
                title = "Teams",
                selectedIcon = ImageVector.vectorResource(R.drawable.teams),
                unselectedIcon = ImageVector.vectorResource(R.drawable.teams),
            ),
            BottomNavigationItem(
                title = "Matches",
                selectedIcon = ImageVector.vectorResource(R.drawable.ball),
                unselectedIcon = ImageVector.vectorResource(R.drawable.ball),
            ),
            BottomNavigationItem(
                title = "Standings",
                selectedIcon = ImageVector.vectorResource(R.drawable.standings),
                unselectedIcon = ImageVector.vectorResource(R.drawable.standings),
            ),
        )
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }
        NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.tertiary,
                    ),
                    selected = selectedItemIndex == index,
                    onClick = {
                        if(selectedItemIndex != index) {
                            selectedItemIndex = index
                            when (index) {
                                0 -> goTeams()
                                1 -> goMatches()
                                2 -> goStandings()
                            }
                        }
                    },
                    label = {
                        Text(text = item.title, color = MaterialTheme.colorScheme.onPrimary)
                    },
                    icon = {
                        Icon(
                            tint = MaterialTheme.colorScheme.onPrimary,
                            imageVector = if (index == selectedItemIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }
                )
            }
        }
    }
}