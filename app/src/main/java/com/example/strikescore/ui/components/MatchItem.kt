package com.example.strikescore.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.strikescore.model.Match
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("SuspiciousIndentation")
@Composable
fun MatchItem(
    modifier: Modifier = Modifier,
    match : Match,
) {

    val imgLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()


    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp, shape = RoundedCornerShape(8.dp)),
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = modifier
                    .size(100.dp)
                    .padding(8.dp),
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(match.homeTeam.crest).crossfade(true).build(),
                    contentDescription = "hometeamcrest",
                    imageLoader = imgLoader,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp),
                )
            }

            Box(
                modifier = modifier
                    .padding(8.dp),
            ){

                if(match.status.equals("FINISHED")){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                        text = match.score.fullTime.home.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(8.dp),
                        )
                        Text(
                            text = "-",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(8.dp),
                        )
                        Text(
                        text = match.score.fullTime.away.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(8.dp),
                        )
                    }

                }else if(match.status.equals("POSTPONED")){
                    Text(
                        text = match.status,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .padding(8.dp),
                    )

                }
                else{
                    Text(
                        text = convertToBelgianTime(match.utcDate).split("T")[1].substring(0,5),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .padding(8.dp),
                    )
                }

            }

            Box(
                modifier = modifier
                    .size(100.dp)
                    .padding(8.dp),
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(match.awayTeam.crest).crossfade(true).build(),
                    contentDescription = "awayteamcrest",
                    imageLoader = imgLoader,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp),
                )
            }

        }
    }
}

fun convertToBelgianTime(utcTime: String): String {
    // Parse the UTC time
    val utcZonedDateTime = ZonedDateTime.parse(utcTime)

    // Define the Belgian time zone (CET/CEST)
    val belgianZoneId = ZoneId.of("Europe/Brussels")

    // Convert the UTC time to Belgian time
    val belgianTime = utcZonedDateTime.withZoneSameInstant(belgianZoneId)

    // Format the Belgian time as a String to return
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
    return belgianTime.format(formatter)
}
