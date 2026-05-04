package com.example.playlistmaker.data.network

import com.example.playlistmaker.creator.Storage
import com.example.playlistmaker.data.playlist.TracksSearchRequest
import com.example.playlistmaker.data.playlist.TracksSearchResponse
import com.example.playlistmaker.domain.NetworkClient

class RetrofitNetworkClient(private val storage: Storage) : NetworkClient {
    override fun doRequest(dto: Any): TracksSearchResponse {
        val searchList = storage.search((dto as TracksSearchRequest).expression)
        return TracksSearchResponse(searchList).apply { resultCode = 200 }
    }

    override fun getAllTracks(): List<Track> {
        return storage.listTracks.map {
            Track(0,it.trackName, it.artistName, it.trackTimeMillis.toString(), null)
        }
    }
}