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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.strikescore.R
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        shape = RectangleShape,
        modifier = modifier
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(dimensionResource(R.dimen.padding_small)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .animateContentSize()
                    .padding(dimensionResource(R.dimen.smallSpacer)),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(
                    text = standing.position.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSecondary,
                )

                //add spacer
                Spacer(
                    modifier = Modifier.size(dimensionResource(R.dimen.smallSpacer))
                )

                AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(standing.team.crest).crossfade(true).build(),
                        contentDescription = stringResource(R.string.standingscrest),
                        imageLoader = imgLoader,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.box_size_small))
                            .padding(dimensionResource(R.dimen.padding_small)),
                    )

                //add spacer
                Spacer(
                    modifier = Modifier.size(dimensionResource(R.dimen.padding_small))
                )

                    Text(
                        text = standing.team.name,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
            }




                Text(
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_small)),
                    text = standing.points.toString(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Right,
                )


        }
    }
}