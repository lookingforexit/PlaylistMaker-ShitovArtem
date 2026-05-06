package com.example.playlistmaker.data.playlist

import com.example.playlistmaker.data.network.Track

data class Playlist(
    val id: Int = 0,
    val name: String,
    val description: String,
    val tracks: List<Track>,
    val image: String?
)