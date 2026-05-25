package com.carmusic.player.player

import com.carmusic.player.data.model.Song

data class PlayerState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0,
    val duration: Long = 0,
    val queue: List<Song> = emptyList(),
    val queueIndex: Int = -1,
    val shuffleMode: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF
)

enum class RepeatMode {
    OFF, ALL, ONE
}
