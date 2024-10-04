package com.example.spotidle.ui.guess

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.spotidle.ArtistData
import com.example.spotidle.TrackInfo
import com.example.spotidle.ui.guess.components.GuessSection
import com.example.spotidle.ui.guess.components.SpotifightScaffold

@Composable
fun ArtistGuessScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    track: TrackInfo,
    artist: ArtistData
) {
    val context = LocalContext.current
    var failedTries by remember { mutableIntStateOf(0) }

    SpotifightScaffold(navController = navController) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size((context.resources.displayMetrics.widthPixels / 4).dp)
                .aspectRatio(1f)
                .background(Color(0xFF1ED760))
        ) {
            when (failedTries) {
                0 -> {
                    Box(
                        modifier = Modifier
                            .size((context.resources.displayMetrics.widthPixels / 4).dp)
                            .aspectRatio(1f)
                            .background(Color(0xFF1ED760)),
                        contentAlignment = Alignment.Center

                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(track.albumCoverUrl),
                            contentDescription = "Album Cover",
                            modifier = Modifier.size(150.dp)
                        )
                    }
                }
                1 -> {
                    artist.mostPopularSong?.let { popularSong ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Hint: Most Popular Song: $popularSong",
                                color = Color.White
                            )
                        }
                    }
                }
                2 -> {
                    artist.profilePicture?.let { profilePicture ->
                        Image(
                            painter = rememberAsyncImagePainter(profilePicture),
                            contentDescription = "Artist Profile Picture",
                            modifier = Modifier.size(150.dp)
                        )
                    }
                }
            }
        }

        GuessSection(
            correctGuessName = track.artist,
            onGuessSubmit = { guess ->
                if (guess == track.artist) {
                    //TODO success
                } else {
                    failedTries++
                }
            }
        )
    }

}