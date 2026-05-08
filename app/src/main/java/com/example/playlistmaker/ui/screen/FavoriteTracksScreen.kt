@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.playlistmaker.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.playlistmaker.ui.viewmodel.FavoriteTracksViewModel

@Composable
fun FavoriteTracksScreen(
    favoriteTracksViewModel: FavoriteTracksViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (Int) -> Unit
) {
    val tracks by favoriteTracksViewModel.tracks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Избранные треки"
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            onBackClick()
                        },
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            )
        },

        ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(
                items = tracks,
                key = { track -> track.trackID }
            ) { track ->
                TrackListItemIn(
                    track = track,
                    onClick = {
                        onTrackClick(it)
                    }
                )
            }
        }

    }
}