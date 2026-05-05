package com.example.playlistmaker.data.playlist

import com.example.playlistmaker.data.network.Track
import com.example.playlistmaker.domain.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class PlaylistsRepositoryImpl(): PlaylistsRepository {
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    private val _favoriteTracks = MutableStateFlow<List<Track>>(emptyList())


    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return _playlists.asStateFlow()
    }

    override fun addPlaylist(name: String, description: String) {
        _playlists.update {
            currentPlaylist -> val newPlaylist = Playlist(
                id = _playlists.value.size + 1,
                name = name,
                description = description,
                tracks = emptyList()
            )
            currentPlaylist + newPlaylist
        }
    }

    override fun getPlaylist(id: Int?): Flow<Playlist?> {
        return _playlists.asStateFlow().map {
            playlistList -> playlistList.find { it.id == id }
        }
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return _favoriteTracks.asStateFlow()
    }

    override fun insertTrackToPlaylist(track: Track, playlistID: Int) {
        _playlists.update {
            val targetPlaylist = _playlists.value.find { it.id == playlistID } ?: return
            if (targetPlaylist.tracks.contains(track)) return
            val updatedTracks = targetPlaylist.tracks + track
            val updatedPlaylist = targetPlaylist.copy(tracks = updatedTracks)
            val newPlaylist = _playlists.value.map {
                if (it.id == targetPlaylist.id) updatedPlaylist else it
            }
            newPlaylist
        }
    }


    override suspend fun deleteTrackFromPlaylist(trackID: Int, playlistID: Int) {
        _playlists.update {
            currentPlaylist ->
            val oldPlaylist = _playlists.value.find { it.id == playlistID } ?: return
            val updatedTracks = oldPlaylist.tracks.filter { it.trackID != trackID }
            val updatedPlaylist = oldPlaylist.copy(tracks = updatedTracks)
            currentPlaylist.map {
                playlist -> if (playlist.id == playlistID) updatedPlaylist else playlist
            }
        }
    }

    override suspend fun toggleFavorite(track: Track) {
        if (track.favorite) _favoriteTracks.value = _favoriteTracks.value - track
        else _favoriteTracks.update {
            it + track
        }
        track.favorite = !track.favorite
    }

    override suspend fun deletePlaylistByID(playlistID: Int) {
        _playlists.update {
            currentPlaylist -> currentPlaylist.filter { it.id != playlistID }
        }
    }
}