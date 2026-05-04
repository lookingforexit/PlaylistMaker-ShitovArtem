package com.example.playlistmaker.data.network

data class Track(
    val trackID: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val playlistID: Int?,
    val favorite: Boolean = false
)