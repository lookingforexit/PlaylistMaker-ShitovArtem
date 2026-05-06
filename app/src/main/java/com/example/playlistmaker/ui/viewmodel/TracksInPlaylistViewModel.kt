package com.example.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.network.Track
import com.example.playlistmaker.domain.PlaylistsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TracksInPlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository
): ViewModel() {
    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks = _tracks.asStateFlow()

    fun getAllTracksInPlaylist(playlistID: Int) {
        viewModelScope.launch {
            _tracks.update {
                playlistsRepository.getAllTracksInPlaylist(playlistID)
            }
        }
    }
}