package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.playlist.TrackSearchByIDResponse
import com.example.playlistmaker.data.playlist.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPIService {
    @GET("/search")
    suspend fun searchTracks(
        @Query("term") query: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 50
    ): TracksSearchResponse

    @GET("/lookup")
    suspend fun lookupTrackByID(
        @Query("id") trackID: Int
    ): TrackSearchByIDResponse
}