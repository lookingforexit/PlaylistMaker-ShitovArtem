package com.example.playlistmaker

import com.example.playlistmaker.data.database.PlaylistEntity
import com.example.playlistmaker.data.database.TrackEntity
import com.example.playlistmaker.data.network.Track
import com.example.playlistmaker.data.playlist.Playlist
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
        image = artworkUrl100?.replace("100x100", "512x512") ?: ""
    )
}

fun TrackEntity.toTrack(): Track{
    return Track(
        trackID = this.trackID,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = this.trackTime,
        favorite = this.favorite,
        image = this.image
    )
}

fun Track.toEntity(): TrackEntity {
    return TrackEntity(
        trackID = this.trackID,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = this.trackTime,
        image = this.image,
        favorite = this.favorite
    )
}

fun Playlist.toPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        playlistID = this.playlistID,
        name = this.name,
        description = this.description,
        image = this.image ?: ""
    )
}

fun PlaylistEntity.toPlaylist(): Playlist {
    return Playlist(
        playlistID = this.playlistID,
        name = this.name,
        description = this.description ?: "",
        image = this.image
    )
}