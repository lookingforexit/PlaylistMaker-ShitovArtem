package com.example.playlistmaker.data.network

data class Track(
    val trackID: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val playlistID: Int?,
    var favorite: Boolean = false,
    val image: String?
)