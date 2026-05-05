package com.example.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.network.Track
import com.example.playlistmaker.data.playlist.Playlist
import com.example.playlistmaker.domain.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class PlaylistsViewModel (
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {
    val playlists: Flow<List<Playlist>> = flow {
        val collectedPlaylists = mutableListOf<Playlist>()
        playlistsRepository.getAllPlaylists().collect {
            playlist -> collectedPlaylists.addAll(playlist)
            emit(collectedPlaylists.toList())
        }
    }

    fun createNewPlaylist(name: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.addPlaylist(name, description)
        }
    }

    suspend fun insertTrackToPlaylist(track: Track, playlistID: Int) {
        playlistsRepository.insertTrackToPlaylist(track, playlistID)
    }

    suspend fun toggleFavorite(track: Track) {
        playlistsRepository.toggleFavorite(track)
    }

    suspend fun deleteTrackFromPlaylist(trackID: Int, playlistID: Int) {
        playlistsRepository.deleteTrackFromPlaylist(trackID, playlistID)
    }

    suspend fun deletePlaylistByID(playlistID: Int) {
        playlistsRepository.deletePlaylistByID(playlistID)
    }
}