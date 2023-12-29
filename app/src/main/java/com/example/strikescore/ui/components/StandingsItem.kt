package com.example.strikescore.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.strikescore.model.Standings

@Composable
fun StandingsItem(
    modifier: Modifier = Modifier,
    standing: Standings
) {

    val imgLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RectangleShape,
        modifier = modifier
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .animateContentSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(
                    text = standing.position.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                //add spacer
                Spacer(
                    modifier = Modifier.size(16.dp)
                )

                AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(standing.team.crest).crossfade(true).build(),
                        contentDescription = "crest",
                        imageLoader = imgLoader,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(8.dp),
                    )

                //add spacer
                Spacer(
                    modifier = Modifier.size(8.dp)
                )

                    Text(
                        text = standing.team.name,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
            }




                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = standing.points.toString(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Right,
                )


        }
    }
}

// The Standings data class should look something like this:
data class Standings(
    val position: Int,
    val team: Team,
    val gamesPlayed: Int,
    val points: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int
)

data class Team(
    val name: String
    // ... include other team-related data if necessary
)