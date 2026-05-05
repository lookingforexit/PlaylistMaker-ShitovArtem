package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.playlist.TrackSearchByIDRequest
import com.example.playlistmaker.data.playlist.TrackSearchByIDResponse
import com.example.playlistmaker.data.playlist.TracksSearchRequest
import com.example.playlistmaker.data.playlist.TracksSearchResponse
import com.example.playlistmaker.domain.NetworkClient
import com.example.playlistmaker.domain.TracksRepository
import com.example.playlistmaker.toTrackModel
import kotlinx.coroutines.delay

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
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
        val resp = networkClient.doRequest(TrackSearchByIDRequest(trackID))
        return (resp as TrackSearchByIDResponse)
            .results
            .first()
            .toTrackModel()
    }
}