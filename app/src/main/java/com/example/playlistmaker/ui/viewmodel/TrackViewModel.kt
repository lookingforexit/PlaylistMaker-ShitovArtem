package com.example.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.network.Track
import com.example.playlistmaker.domain.PlaylistsRepository
import com.example.playlistmaker.domain.TracksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed class TrackScreenState {
    class Loading: TrackScreenState()
    class Error(val message: String): TrackScreenState()
    class Success(track: Track): TrackScreenState()
}

class TrackViewModel(
    val playlistsRepository: PlaylistsRepository,
    val tracksRepository: TracksRepository
) : ViewModel() {
    private val _screenState = MutableStateFlow<TrackScreenState>(TrackScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _track = MutableStateFlow<Track?>(null)
    fun getTrack() = _track.asStateFlow()

    val scope = viewModelScope
    fun addTrackToFavorite(track: Track) {
        scope.launch {
            playlistsRepository.toggleFavorite(track)
        }
    }

    fun addTrackInPlaylist(track: Track, playlistID: Int) {
        scope.launch {
            playlistsRepository.insertTrackToPlaylist(track, playlistID)
        }
    }

    fun getTrackByID(trackID: Int) {
        try {
            _screenState.update { TrackScreenState.Loading() }
            viewModelScope.launch {
                val response = tracksRepository.getTrackByID(trackID)
                _track.update { response }
                _screenState.update { TrackScreenState.Success(response) }
            }
        } catch (e: IOException) {
            _screenState.update { TrackScreenState.Error(e.message ?: "Unknown exception") }
        }
    }
}