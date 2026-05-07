package com.example.playlistmaker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.playlistmaker.data.database.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksDAO {
    @Upsert
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks WHERE trackName = :name AND artistName = :artist")
    fun getTrackByNameAndArtist(name: String, artist: String): Flow<TrackEntity?>

    @Delete
    suspend fun deleteTrackFromAllPlaylist(track: TrackEntity)

    @Query("DELETE FROM table_link WHERE trackID = :trackID AND playlistID = :playlistID")
    suspend fun deleteTrackFromPlaylist(trackID: Int, playlistID: Int)

    @Query("Select * from tracks where favorite = 1")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM tracks WHERE trackID = :trackID")
    suspend fun getTrackByID(trackID: Int): TrackEntity?
}