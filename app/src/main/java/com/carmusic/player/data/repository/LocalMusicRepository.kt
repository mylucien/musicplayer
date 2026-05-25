package com.carmusic.player.data.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.carmusic.player.data.local.AppDatabase
import com.carmusic.player.data.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalMusicRepository(private val context: Context) {

    private val songDao = AppDatabase.getInstance(context).songDao()

    fun getAllSongs(): Flow<List<Song>> = songDao.getLocalSongs()

    suspend fun scanMediaStore(): List<Song> = withContext(Dispatchers.IO) {
        val songs = mutableListOf<Song>()
        val collection = if (android.os.Build.VERSION.SDK_INT >= 29) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DATE_ADDED
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            collection, projection, selection, null, sortOrder
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)

            while (cursor.moveToNext()) {
                val albumId = cursor.getLong(albumIdCol)
                val coverUri = Uri.parse(
                    "content://media/external/audio/albumart/$albumId"
                )

                songs.add(
                    Song(
                        id = cursor.getLong(idCol),
                        title = cursor.getString(titleCol) ?: "未知",
                        artist = cursor.getString(artistCol) ?: "未知",
                        album = cursor.getString(albumCol) ?: "未知",
                        duration = cursor.getLong(durCol),
                        path = cursor.getString(dataCol) ?: "",
                        coverUrl = coverUri.toString(),
                        isLocal = true,
                        dateAdded = cursor.getLong(dateCol) * 1000
                    )
                )
            }
        }
        songs
    }

    suspend fun refreshLocalSongs() {
        val songs = scanMediaStore()
        songDao.deleteAllBySource(isLocal = true)
        songDao.insertSongs(songs)
    }
}
