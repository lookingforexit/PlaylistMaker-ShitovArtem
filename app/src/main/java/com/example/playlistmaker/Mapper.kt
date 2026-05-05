package com.example.playlistmaker

import com.example.playlistmaker.data.network.Track
import com.example.playlistmaker.data.playlist.TrackDto

fun TrackDto.toTrackModel(): Track {
    val seconds = trackTimeMillis / 1000
    val minutes = seconds / 60
    val trackTime = "%02d".format(minutes) + ":" + "%02d".format(seconds - minutes * 60)
    return Track(
        trackID = id,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        playlistID = null,
        image = artworkUrl100?.replace("100x100", "512x512")
    )
}