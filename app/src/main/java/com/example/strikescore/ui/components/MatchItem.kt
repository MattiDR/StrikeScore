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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.strikescore.R
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
            containerColor = MaterialTheme.colorScheme.secondary,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small))
            .shadow(
                dimensionResource(R.dimen.padding_small),
                shape = RoundedCornerShape(dimensionResource(R.dimen.padding_small))
            ),
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(dimensionResource(R.dimen.smallSpacer)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = modifier
                    .size(dimensionResource(R.dimen.box_size_medium))
                    .padding(dimensionResource(R.dimen.smallSpacer)),
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(match.homeTeam.crest).crossfade(true).build(),
                    contentDescription = stringResource(R.string.hometeamcrest),
                    imageLoader = imgLoader,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.box_size_medium))
                        .padding(dimensionResource(R.dimen.smallSpacer)),
                )
            }

            Box(
                modifier = modifier
                    .padding(dimensionResource(R.dimen.smallSpacer)),
            ){

                if(match.status.equals("FINISHED")){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            color = MaterialTheme.colorScheme.onSecondary,
                        text = match.score.fullTime.home.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.smallSpacer)),
                        )
                        Text(
                            color = MaterialTheme.colorScheme.onSecondary,
                            text = stringResource(R.string.score_spacer),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.smallSpacer)),
                        )
                        Text(
                            color = MaterialTheme.colorScheme.onSecondary,
                        text = match.score.fullTime.away.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.smallSpacer)),
                        )
                    }

                }else if(match.status.equals("POSTPONED")){
                    Text(
                        color = MaterialTheme.colorScheme.onSecondary,
                        text = match.status,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.smallSpacer)),
                    )

                }
                else{
                    Text(
                        color = MaterialTheme.colorScheme.onSecondary,
                        text = convertToBelgianTime(match.utcDate).split("T")[1].substring(0,5),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.smallSpacer)),
                    )
                }

            }

            Box(
                modifier = modifier
                    .size(dimensionResource(R.dimen.box_size_medium))
                    .padding(dimensionResource(R.dimen.smallSpacer)),
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(match.awayTeam.crest).crossfade(true).build(),
                    contentDescription = stringResource(R.string.awayteamcrest),
                    imageLoader = imgLoader,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.box_size_medium))
                        .padding(dimensionResource(R.dimen.smallSpacer)),
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
