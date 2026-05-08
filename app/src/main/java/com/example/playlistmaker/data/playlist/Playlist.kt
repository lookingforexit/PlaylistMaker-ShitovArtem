package com.example.playlistmaker.data.playlist

data class Playlist(
    val playlistID: Int = 0,
    val name: String,
    val description: String,
    val image: String?
)