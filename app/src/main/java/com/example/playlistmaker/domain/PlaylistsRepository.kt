package com.example.playlistmaker.domain

import com.example.playlistmaker.data.playlist.Playlist
import com.example.playlistmaker.data.network.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addPlaylist(name: String, description: String, image: String?)

    suspend fun deletePlaylist(playlistID: Int)

    suspend fun getPlaylist(playlistID: Int): Playlist

    fun getCountTracksInPlaylist(playlistID: Int): Flow<Int>
    fun getAllTracksInPlaylist(playlistID: Int): Flow<List<Track>>
}