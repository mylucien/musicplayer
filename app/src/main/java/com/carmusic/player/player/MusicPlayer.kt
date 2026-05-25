package com.carmusic.player.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.carmusic.player.data.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicPlayer(context: Context) {

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state.asStateFlow()

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _state.value = _state.value.copy(isPlaying = isPlaying)
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val index = exoPlayer.currentMediaItemIndex
                val queue = _state.value.queue
                if (index in queue.indices) {
                    _state.value = _state.value.copy(
                        currentSong = queue[index],
                        queueIndex = index
                    )
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    _state.value = _state.value.copy(
                        duration = exoPlayer.duration.coerceAtLeast(0)
                    )
                }
            }
        })
    }

    fun play(song: Song) {
        val mediaItem = MediaItem.Builder()
            .setMediaId(song.id.toString())
            .setUri(song.path)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(song.title)
                    .setArtist(song.artist)
                    .setAlbumTitle(song.album)
                    .build()
            )
            .build()
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
        _state.value = _state.value.copy(
            currentSong = song,
            queue = listOf(song),
            queueIndex = 0,
            duration = 0
        )
    }

    fun playQueue(queue: List<Song>, startIndex: Int = 0) {
        val mediaItems = queue.map { song ->
            MediaItem.Builder()
                .setMediaId(song.id.toString())
                .setUri(song.path)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .setAlbumTitle(song.album)
                        .build()
                )
                .build()
        }
        exoPlayer.setMediaItems(mediaItems, startIndex, 0L)
        exoPlayer.prepare()
        exoPlayer.play()
        _state.value = _state.value.copy(
            currentSong = queue.getOrNull(startIndex),
            queue = queue,
            queueIndex = startIndex
        )
    }

    fun togglePlay() {
        if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
    }

    fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
        _state.value = _state.value.copy(currentPosition = positionMs)
    }

    fun next() {
        exoPlayer.seekToNextMediaItem()
    }

    fun previous() {
        exoPlayer.seekToPreviousMediaItem()
    }

    fun release() {
        exoPlayer.release()
    }

    fun getExoPlayer(): ExoPlayer = exoPlayer

    companion object {
        @Volatile
        private var instance: MusicPlayer? = null

        fun getInstance(context: Context): MusicPlayer {
            return instance ?: synchronized(this) {
                instance ?: MusicPlayer(context.applicationContext).also { instance = it }
            }
        }
    }
}
