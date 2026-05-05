package com.example.playlistmaker.data.playlist

data class TrackSearchByIDResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : BaseResponse()