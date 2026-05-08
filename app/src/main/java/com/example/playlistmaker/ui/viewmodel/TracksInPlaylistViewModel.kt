package com.example.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.network.Track
import com.example.playlistmaker.domain.PlaylistsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TracksInPlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository
): ViewModel() {
    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks = _tracks.asStateFlow()
    val tracksCount = tracks.map {
        it.size
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    fun getAllTracksInPlaylist(playlistID: Int) {
        viewModelScope.launch {
            playlistsRepository.getAllTracksInPlaylist(playlistID).collect { newList ->
                _tracks.update {
                    newList
                }
            }
        }
    }

    fun deletePlaylist(playlistID: Int, onDeleted: () -> Unit) {
        viewModelScope.launch {
            playlistsRepository.deletePlaylist(playlistID)
            onDeleted()
        }
    }
}
