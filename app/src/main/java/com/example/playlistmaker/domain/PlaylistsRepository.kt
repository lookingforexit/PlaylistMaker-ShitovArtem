package com.example.playlistmaker.domain

import com.example.playlistmaker.data.playlist.Playlist
import com.example.playlistmaker.data.network.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getAllPlaylists(): Flow<List<Playlist>>
    fun addPlaylist(name: String, description: String)
    fun getPlaylist(id: Int?): Flow<Playlist?>
    fun getFavoriteTracks(): Flow<List<Track>>
    fun insertTrackToPlaylists(track: Track, playlistID: Int)

    suspend fun deletePlaylist()
    suspend fun deleteTrackFromPlaylist(trackID: Int, playlistID: Int)
    suspend fun toggleFavorite(track: Track)
    suspend fun deletePlaylistByID(playlistID: Int)
}