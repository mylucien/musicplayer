package com.carmusic.player.data.repository

import com.carmusic.player.data.local.AppDatabase
import com.carmusic.player.data.model.Song
import com.carmusic.player.data.remote.WebDavClient
import com.carmusic.player.data.remote.WebDavConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class WebDavRepository(private val database: AppDatabase) {

    private val songDao = database.songDao()
    private var client: WebDavClient? = null

    fun getAllWebDavSongs(): Flow<List<Song>> = songDao.getWebDavSongs()

    fun connect(config: WebDavConfig): Result<Boolean> {
        return try {
            client = WebDavClient.getInstance(config)
            client!!.testConnection()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun refreshSongs(config: WebDavConfig): Result<Int> = withContext(Dispatchers.IO) {
        runCatching {
            val davClient = WebDavClient.getInstance(config)
            val audioResult = davClient.listAudioFiles()

            if (audioResult.isFailure) throw audioResult.exceptionOrNull()!!

            val files = audioResult.getOrThrow()
            val songs = files.map { file ->
                Song(
                    id = file.path.hashCode().toLong(),
                    title = file.name.substringBeforeLast('.'),
                    artist = "WebDAV",
                    album = config.name,
                    duration = 0, // WebDAV 不直接提供时长，播放时获取
                    path = file.path,
                    isLocal = false,
                    sourceId = config.url,
                    dateAdded = System.currentTimeMillis()
                )
            }

            songDao.deleteAllBySource(isLocal = false)
            songDao.insertSongs(songs)
            songs.size
        }
    }
}
