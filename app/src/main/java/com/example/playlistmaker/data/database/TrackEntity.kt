package com.example.playlistmaker.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val trackID: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val image: String,
    val favorite: Boolean = false
)