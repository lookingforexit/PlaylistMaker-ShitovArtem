package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.playlist.BaseResponse
import com.example.playlistmaker.data.playlist.TrackSearchByIDRequest
import com.example.playlistmaker.data.playlist.TracksSearchRequest
import com.example.playlistmaker.domain.NetworkClient
import java.io.IOException

class RetrofitNetworkClient(private val apiService: ITunesAPIService) : NetworkClient {
    override suspend fun doRequest(dto: Any): BaseResponse {
        return try {
            when (dto) {
                is TracksSearchRequest -> {
                    apiService.searchTracks(
                        query = dto.expression,
                        media = "music",
                        entity = "song",
                        limit = 50
                    ).apply { resultCode = 200 }
                }
                is TrackSearchByIDRequest -> {
                    apiService.lookupTrackByID(trackID = dto.trackID).apply { resultCode = 200 }
                }
                else -> {
                    BaseResponse().apply { resultCode = 400 }
                }
            }
        }
        catch (e: IOException) {
            BaseResponse().apply { resultCode = -1 }
        } catch (e: Exception) {
            BaseResponse().apply { resultCode = -2 }
        }
    }
}