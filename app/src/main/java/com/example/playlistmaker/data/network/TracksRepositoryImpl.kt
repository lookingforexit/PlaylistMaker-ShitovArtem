package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.database.AppDatabase
import com.example.playlistmaker.data.database.TableLinkEntity
import com.example.playlistmaker.data.playlist.TrackSearchByIDRequest
import com.example.playlistmaker.data.playlist.TrackSearchByIDResponse
import com.example.playlistmaker.data.playlist.TracksSearchRequest
import com.example.playlistmaker.data.playlist.TracksSearchResponse
import com.example.playlistmaker.domain.NetworkClient
import com.example.playlistmaker.domain.TracksRepository
import com.example.playlistmaker.toEntity
import com.example.playlistmaker.toTrack
import com.example.playlistmaker.toTrackModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    db: AppDatabase
) : TracksRepository {
    private val tracksDAO = db.TracksDAO()
    private val linkDAO = db.TableLinkDAO()

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        delay(100) // Эммулируем задержку ответа
        return if (response.resultCode == 200) { // успешный запрос
            (response as TracksSearchResponse).results.map {
                it.toTrackModel()
            }
        } else {
            emptyList()
        }
    }

    override suspend fun getTrackByID(trackID: Int): Track {
        val track = tracksDAO.getTrackByID(trackID)?.toTrack()
        if (track == null) {
            val response = networkClient.doRequest(TrackSearchByIDRequest(trackID))
            return (response as TrackSearchByIDResponse)
                .results
                .first()
                .toTrackModel()
        }
        return track
    }

    override suspend fun updateTrackFavoriteStatus(track: Track) {
        val newFavoriteStatus = !track.favorite
        val entity = track.copy(favorite = newFavoriteStatus).toEntity()
        tracksDAO.insertTrack(entity)
    }

    override suspend fun deleteTrackFromAllPlaylists(track: Track) {
        tracksDAO.deleteTrackFromAllPlaylist(track.toEntity())
    }

    override suspend fun deleteTrackFromPlaylist(trackID: Int, playlistID: Int) {
        linkDAO.deleteLink(trackID, playlistID)
    }

    override suspend fun insertTrackToPlaylist(
        track: Track,
        playlistID: Int
    ) {
        tracksDAO.insertTrack(track.toEntity())
        linkDAO.addLink(TableLinkEntity(track.trackID, playlistID))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return tracksDAO.getFavoriteTracks().map {
            it.map {
                it.toTrack()
            }
        }
    }

}