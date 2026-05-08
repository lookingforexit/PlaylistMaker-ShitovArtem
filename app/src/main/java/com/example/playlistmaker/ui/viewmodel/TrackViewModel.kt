package com.example.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.network.Track
import com.example.playlistmaker.data.playlist.Playlist
import com.example.playlistmaker.domain.PlaylistsRepository
import com.example.playlistmaker.domain.TracksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed class TrackScreenState {
    class Loading: TrackScreenState()
    class Error(val message: String): TrackScreenState()
    class Success(val track: Track): TrackScreenState()
}

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class TrackViewModel(
    val playlistsRepository: PlaylistsRepository,
    val tracksRepository: TracksRepository
) : ViewModel() {
    private val _screenState = MutableStateFlow<TrackScreenState>(TrackScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _track = MutableStateFlow<Track?>(null)
    val playlistsWithCounts =
        playlistsRepository.getAllPlaylists()
            .flatMapLatest { playlistList ->
                val countFlows = playlistList.map { playlist ->
                    playlistsRepository.getCountTracksInPlaylist(playlist.playlistID)
                        .map { count -> playlist to count }
                }
                if (countFlows.isEmpty()) flowOf(emptyList())
                else combine(countFlows) { it.toList() }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val scope = viewModelScope
    fun addTrackToFavorite(track: Track) {
        scope.launch {
            tracksRepository.updateTrackFavoriteStatus(track)
            val updatedTrack = track.copy(favorite = !track.favorite)
            _track.update { updatedTrack }
            _screenState.update { TrackScreenState.Success(updatedTrack) }
        }
    }

    fun addTrackToPlaylist(track: Track, playlistID: Int) {
        scope.launch {
            tracksRepository.insertTrackToPlaylist(track, playlistID)
        }
    }

    fun getTrackByID(trackID: Int) {
        _screenState.update { TrackScreenState.Loading() }
        viewModelScope.launch {
            try {
                val response = tracksRepository.getTrackByID(trackID)
                _track.update { response }
                _screenState.update { TrackScreenState.Success(response) }
            } catch (e: IOException) {
                _screenState.update { TrackScreenState.Error(e.message ?: "Unknown exception") }
            }
        }
    }
}
