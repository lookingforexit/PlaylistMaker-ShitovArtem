package com.example.playlistmaker.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "table_link",
    primaryKeys = ["trackID", "playlistID"],
    foreignKeys = [
        ForeignKey(
            entity = TrackEntity::class,
            parentColumns = ["trackID"],
            childColumns = ["trackID"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["playlistID"],
            childColumns = ["playlistID"],
            onDelete = CASCADE
        )
    ]
)
class TableLinkEntity(
    val trackID: Int,
    val playlistID: Int
)