package com.example.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.playlist.Playlist
import com.example.playlistmaker.domain.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class PlaylistsViewModel (
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {
    val playlistsWithCounts: StateFlow<List<Pair<Playlist, Int>>> =
        playlistsRepository.getAllPlaylists()
            .catch { emit(emptyList()) }
            .flatMapLatest { playlistList ->
                val countFlows = playlistList.map { playlist ->
                    playlistsRepository.getCountTracksInPlaylist(playlist.playlistID)
                        .catch { emit(0) }
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


    fun createNewPlaylist(name: String, description: String, image: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.addPlaylist(name, description, image)
        }
    }

    suspend fun deletePlaylistByID(playlistID: Int) {
        playlistsRepository.deletePlaylist(playlistID)
    }
}
