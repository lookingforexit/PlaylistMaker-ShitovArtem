package com.example.playlistmaker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.dao.PlaylistsDAO
import com.example.playlistmaker.data.dao.TableLinkDAO
import com.example.playlistmaker.data.dao.TracksDAO

@Database(
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        TableLinkEntity::class
    ], version = 3, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun TracksDAO(): TracksDAO
    abstract fun PlaylistsDAO(): PlaylistsDAO
    abstract fun TableLinkDAO(): TableLinkDAO
}
