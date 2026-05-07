package com.example.playlistmaker.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist",
    indices = [Index(value = ["name"], unique = true)]
)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistID: Int = 0,
    val name: String,
    val description: String?,
    val image: String
)