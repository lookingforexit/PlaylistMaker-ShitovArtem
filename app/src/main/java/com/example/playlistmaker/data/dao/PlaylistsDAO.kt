package com.example.playlistmaker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.database.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Query("Select * from playlist")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("Delete from playlist where playlistID = :playlistID")
    suspend fun deletePlaylist(playlistID: Int)

    @Query("Select * from playlist where playlistID = :playlistID")
    suspend fun getPlaylist(playlistID: Int): PlaylistEntity

}