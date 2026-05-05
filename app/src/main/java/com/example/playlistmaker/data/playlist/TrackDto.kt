package com.example.playlistmaker.data.playlist

import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("trackId") val id: Int,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val trackTimeMillis: Int,
    @SerializedName("artworkUrl100") val artworkUrl100: String?,
    @SerializedName("previewUrl") val previewUrl: String?,
)