package com.carmusic.player.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey
    val id: Long = 0,
    val title: String = "未知歌曲",
    val artist: String = "未知艺术家",
    val album: String = "未知专辑",
    val duration: Long = 0,          // 毫秒
    val path: String = "",           // 本地路径或 WebDAV URL
    val coverUrl: String = "",
    val isLocal: Boolean = true,     // true=本地, false=WebDAV
    val sourceId: String = "",       // WebDAV 服务器标识
    val dateAdded: Long = System.currentTimeMillis()
)

data class Album(
    val id: Long = 0,
    val name: String = "未知专辑",
    val artist: String = "未知艺术家",
    val coverUrl: String = "",
    val songCount: Int = 0
)

data class Playlist(
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val coverUrl: String = "",
    val songCount: Int = 0,
    val songs: List<Song> = emptyList()
)
