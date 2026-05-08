package com.example.playlistmaker.domain

import com.example.playlistmaker.data.network.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
    suspend fun getTrackByID(trackID: Int): Track
    suspend fun updateTrackFavoriteStatus(track: Track)
    suspend fun deleteTrackFromAllPlaylists(track: Track)
    suspend fun deleteTrackFromPlaylist(trackID: Int, playlistID: Int)
    suspend fun insertTrackToPlaylist(track: Track, playlistID: Int)
    fun getFavoriteTracks(): Flow<List<Track>>
}