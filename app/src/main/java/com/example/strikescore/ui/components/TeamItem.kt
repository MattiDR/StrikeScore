package com.example.strikescore.ui.components

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.strikescore.model.Team


@SuppressLint("SuspiciousIndentation")
@Composable
fun TeamItem(
    modifier: Modifier = Modifier,
    team: Team,
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
                .padding(8.dp)
                .shadow(8.dp, shape = RoundedCornerShape(8.dp)),
        ) {
            Row(
                modifier = Modifier
                    .animateContentSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                       model = ImageRequest.Builder(LocalContext.current).data(team.crest).crossfade(true).build(),
                       contentDescription = "crest",
                       imageLoader = imgLoader,
                          modifier = Modifier
                              .size(100.dp)
                              .padding(8.dp),
                )

                Spacer(
                    modifier = Modifier.size(16.dp)
                )

                Column {
                    Text(
                        text = team.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )

                    Text(
                        text = team.tla,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )


                }


            }
        }
}