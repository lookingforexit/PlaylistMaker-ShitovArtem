@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.playlistmaker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.example.playlistmaker.data.playlist.Playlist
import com.example.playlistmaker.ui.viewmodel.PlaylistsViewModel

fun getTracksCountString(tracksCount: Int): String {
    val prefix = "$tracksCount "
    return when {
        tracksCount % 10 == 1 && tracksCount % 100 != 11 -> prefix + "трек"
        tracksCount % 10 in 2..4 && tracksCount % 100 !in 12..14 -> prefix + "трека"
        else -> prefix + "треков"
    }
}

@Composable
fun PlaylistListItem(
    modifier: Modifier = Modifier,
    playlist: Playlist,
    tracksCount: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = { onClick() }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageModifier = Modifier
            .size(45.dp)
            .clip(RoundedCornerShape(2.dp))

        Box(
            modifier = imageModifier.background(Color.LightGray.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = playlist.image?.takeUnless { it.isBlank() },
                contentDescription = "Плейлист ${playlist.name}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Album",
                        modifier = Modifier
                            .size(22.dp)
                            .alpha(0.4f),
                        tint = Color.Gray
                    )
                },
                error = {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Album",
                        modifier = Modifier
                            .size(22.dp)
                            .alpha(0.4f),
                        tint = Color.Gray
                    )
                }
            )
        }
        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = playlist.name,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = getTracksCountString(tracksCount),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun PlaylistsScreen(
    modifier: Modifier = Modifier,
    playlistsViewModel: PlaylistsViewModel,
    addNewPlaylist: () -> Unit,
    navigateToPlaylist: (Int) -> Unit,
    navigateBack: () -> Unit
) {
    val playlistsWithCounts by playlistsViewModel.playlistsWithCounts.collectAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    Text(
                        modifier =  Modifier.padding(horizontal = 12.dp),
                        text = "Плейлисты",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(32.dp)
                            .clickable {
                                navigateBack()
                            },
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addNewPlaylist()
                },
                modifier = Modifier.padding(bottom = 16.dp, end = 8.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new playlist"
                    )
                },
                shape = CircleShape
            )
        }
    ) {innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding
        ) {
            items(playlistsWithCounts) { item ->
                val (playlist, count) = item
                PlaylistListItem(
                    playlist = playlist,
                    onClick = {
                        navigateToPlaylist(playlist.playlistID)
                    },
                    tracksCount = count
                )
            }
        }
    }

}
