package com.example.playlistmaker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.playlistmaker.R
import com.example.playlistmaker.data.network.Track
import com.example.playlistmaker.data.playlist.Playlist
import com.example.playlistmaker.ui.viewmodel.TrackScreenState
import com.example.playlistmaker.ui.viewmodel.TrackViewModel
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackScreen(
    trackViewModel: TrackViewModel,
    onBackClick: () -> Unit
) {
    val trackScreenState by trackViewModel.screenState.collectAsState()
    var isLoaded by remember { mutableStateOf(false) }

    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val playlistsWithCounts by trackViewModel.playlistsWithCounts.collectAsState()


    when (trackScreenState) {
        is TrackScreenState.Error -> {
            Text(text = "Error: ${(trackScreenState as TrackScreenState.Error).message}")
        }

        is TrackScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        }

        is TrackScreenState.Success -> {
            val track = (trackScreenState as TrackScreenState.Success).track
            LaunchedEffect(track.trackID) {
                isLoaded = false
            }
            PlaylistsSheet(
                track = track,
                showBottomSheet = showBottomSheet,
                sheetState = sheetState,
                onDismiss = {
                    showBottomSheet = false
                },
                playlistsWithCounts = playlistsWithCounts,
                addTrackToPlaylist = {
                        track, playlistID ->
                    trackViewModel.addTrackToPlaylist(track, playlistID)
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TopAppBar(
                    modifier = Modifier,
                    title = {
                        Text(text = "")
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(32.dp)
                                .clickable { onBackClick() },
                        )
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 26.dp)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        if (!isLoaded) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .shimmer()
                                    .background(Color.Gray.copy(alpha = 0.3f))
                            )
                        }

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(track.image)
                                .crossfade(true)
                                .listener(
                                    onStart = {
                                        isLoaded = false
                                    },
                                    onSuccess = { _, _ ->
                                        isLoaded = true
                                    },
                                    onError = { _, _ ->
                                        isLoaded = true
                                    }
                                ).build(),
                            contentDescription = "Album",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 26.dp),
                        text = track.trackName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = track.artistName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(52.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FloatingActionButton(
                            onClick = {
                                showBottomSheet = true
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.AddToPhotos,
                                    contentDescription = "Add to favorites",
                                    tint = Color.White
                                )
                            },
                            shape = CircleShape,
                            containerColor = Color.LightGray
                        )

                        FloatingActionButton(
                            onClick = {
                                trackViewModel.addTrackToFavorite(track)
                            },
                            content = {
                                Icon(
                                    imageVector = if (track.favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Add to favorites",
                                    tint = Color.White
                                )
                            },
                            shape = CircleShape,
                            containerColor = Color.LightGray
                        )
                    }

                    Spacer(Modifier.height(26.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Длительность",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = track.trackTime,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistsSheet(
    track: Track,
    showBottomSheet: Boolean,
    sheetState: SheetState,
    addTrackToPlaylist: (Track, Int) -> Unit,
    onDismiss: () -> Unit,
    playlistsWithCounts: List<Pair<Playlist, Int>>
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismiss()
            },
            sheetState = sheetState,
            dragHandle = {
                BottomSheetDefaults.DragHandle()
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                    text = "Добавить в плейлист",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(playlistsWithCounts) { (playlist, tracksCount) ->
                        PlaylistItem(
                            playlist = playlist,
                            tracksCount = tracksCount,
                            onClick = {
                                addTrackToPlaylist(track, playlist.playlistID)
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PlaylistItem(
    playlist: Playlist,
    tracksCount: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically

    ) {
        AsyncImage(
            model = playlist.image,
            placeholder = painterResource(R.drawable.ic_music_icon),
            error = painterResource(R.drawable.ic_music_icon),
            contentDescription = "Playlist",
            modifier = Modifier
                .size(45.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = playlist.name,
                fontSize = 16.sp,
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
