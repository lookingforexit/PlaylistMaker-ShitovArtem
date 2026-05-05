package com.example.playlistmaker.domain

import com.example.playlistmaker.data.playlist.BaseResponse
import com.example.playlistmaker.data.network.Track

interface NetworkClient {
    suspend fun doRequest(dto: Any): BaseResponse
}