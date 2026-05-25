package com.carmusic.player.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.carmusic.player.data.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Query("SELECT * FROM songs ORDER BY dateAdded DESC")
    fun getAllSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE isLocal = 1 ORDER BY dateAdded DESC")
    fun getLocalSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE isLocal = 0 ORDER BY dateAdded DESC")
    fun getWebDavSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
    fun searchSongs(query: String): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE id = :id")
    suspend fun getSongById(id: Long): Song?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<Song>)

    @Delete
    suspend fun deleteSong(song: Song)

    @Query("DELETE FROM songs WHERE isLocal = :isLocal")
    suspend fun deleteAllBySource(isLocal: Boolean)

    @Query("SELECT DISTINCT artist FROM songs ORDER BY artist")
    fun getAllArtists(): Flow<List<String>>
}
