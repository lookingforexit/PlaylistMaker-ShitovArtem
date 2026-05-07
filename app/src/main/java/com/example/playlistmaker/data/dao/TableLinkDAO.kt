package com.example.playlistmaker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.database.TableLinkEntity
import com.example.playlistmaker.data.database.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TableLinkDAO {
    @Query("""
        SELECT tracks.* FROM tracks
        INNER JOIN table_link ON tracks.trackID = table_link.trackID
        WHERE table_link.playlistID = :playlistID
    """)
    fun getTracksForPlaylist(playlistID: Int): Flow<List<TrackEntity>>

    @Query("Delete from table_link WHERE trackID = :trackID AND playlistID = :playlistID")
    suspend fun deleteLink(trackID: Int, playlistID: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLink(link: TableLinkEntity)

    @Query("SELECT COUNT(*) FROM table_link WHERE playlistID = :playlistID")
    fun getTracksCount(playlistID: Int): Flow<Int>
}