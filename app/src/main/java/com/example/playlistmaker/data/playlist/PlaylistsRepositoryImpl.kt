package com.example.playlistmaker.data.playlist

import com.example.playlistmaker.data.database.AppDatabase
import com.example.playlistmaker.data.database.PlaylistEntity
import com.example.playlistmaker.data.network.Track
import com.example.playlistmaker.domain.PlaylistsRepository
import com.example.playlistmaker.toPlaylist
import com.example.playlistmaker.toTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    database: AppDatabase
): PlaylistsRepository {
    private val playlistsDAO = database.PlaylistsDAO()
    private val linkDAO = database.TableLinkDAO()


    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistsDAO.getAllPlaylists().map {
            it.map {
                it.toPlaylist()
            }
        }
    }

    override suspend fun addPlaylist(name: String, description: String, image: String?) {
        val playlist = PlaylistEntity(
            name = name,
            description = description,
            image = image ?: ""
        )
        playlistsDAO.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlistID: Int) {
        playlistsDAO.deletePlaylist(playlistID)
    }

    override suspend fun getPlaylist(playlistID: Int): Playlist {
        return playlistsDAO.getPlaylist(playlistID).toPlaylist()
    }

    override fun getAllTracksInPlaylist(playlistID: Int): Flow<List<Track>> {
        return linkDAO.getTracksForPlaylist(playlistID).map {
            it.map {
                it.toTrack()
            }
        }
    }

    override fun getCountTracksInPlaylist(playlistID: Int): Flow<Int> {
        return linkDAO.getTracksCount(playlistID)
    }
}