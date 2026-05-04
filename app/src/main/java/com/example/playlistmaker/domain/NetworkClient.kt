package com.example.playlistmaker.domain

import com.example.playlistmaker.data.playlist.BaseResponse
import com.example.playlistmaker.data.network.Track

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
    fun getAllTracks(): List<Track>
}