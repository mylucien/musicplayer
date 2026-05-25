package com.carmusic.player.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.carmusic.player.data.local.AppDatabase
import com.carmusic.player.data.model.Song
import com.carmusic.player.data.remote.WebDavConfig
import com.carmusic.player.data.repository.LocalMusicRepository
import com.carmusic.player.data.repository.WebDavRepository
import com.carmusic.player.player.MusicPlayer
import com.carmusic.player.player.PlayerState
import com.carmusic.player.player.RepeatMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    val player: MusicPlayer = MusicPlayer.getInstance(application)
    private val localRepo = LocalMusicRepository(application)
    private val webDavRepo = WebDavRepository(AppDatabase.getInstance(application))

    // 播放状态
    val playerState: StateFlow<PlayerState> = player.state

    // 歌曲列表
    val localSongs = localRepo.getAllSongs()
    val webDavSongs = webDavRepo.getAllWebDavSongs()

    // 页面导航
    private val _currentPage = MutableStateFlow("home")
    val currentPage: StateFlow<String> = _currentPage.asStateFlow()

    // 搜索状态
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // 搜索结果
    private val _searchResults = MutableStateFlow<List<Song>>(emptyList())
    val searchResults: StateFlow<List<Song>> = _searchResults.asStateFlow()

    // WebDAV 配置
    private val _webDavConfig = MutableStateFlow(
        WebDavConfig()
    )
    val webDavConfig: StateFlow<WebDavConfig> = _webDavConfig.asStateFlow()

    // 扫描状态
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    // WebDAV 连接状态
    private val _webDavConnected = MutableStateFlow(false)
    val webDavConnected: StateFlow<Boolean> = _webDavConnected.asStateFlow()

    // 收藏歌曲 ID 集合
    private val _favoriteIds = MutableStateFlow<Set<Long>>(emptySet())
    val favoriteIds: StateFlow<Set<Long>> = _favoriteIds.asStateFlow()

    init {
        // 初始化时扫描本地歌曲
        refreshLocalSongs()
    }

    // === 导航 ===
    fun navigateTo(page: String) {
        _currentPage.value = page
    }

    // === 搜索 ===
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _searchResults.value = emptyList()
        }
    }

    fun performSearch() {
        val query = _searchQuery.value.trim()
        if (query.isBlank()) return
        viewModelScope.launch {
            // 从本地库搜索
            val db = AppDatabase.getInstance(getApplication())
            db.songDao().searchSongs(query).collect { results ->
                _searchResults.value = results
            }
        }
    }

    // === 本地扫描 ===
    fun refreshLocalSongs() {
        viewModelScope.launch {
            _isScanning.value = true
            localRepo.refreshLocalSongs()
            _isScanning.value = false
        }
    }

    // === WebDAV ===
    fun connectWebDav(config: WebDavConfig) {
        viewModelScope.launch {
            val result = webDavRepo.connect(config)
            _webDavConnected.value = result.isSuccess
            if (result.isSuccess) {
                _webDavConfig.value = config
                _isScanning.value = true
                webDavRepo.refreshSongs(config)
                _isScanning.value = false
            }
        }
    }

    // === 播放 ===
    fun playSong(song: Song) {
        player.play(song)
    }

    fun playQueue(songs: List<Song>, startIndex: Int = 0) {
        player.playQueue(songs, startIndex)
    }

    fun togglePlay() = player.togglePlay()

    fun next() = player.next()

    fun previous() = player.previous()

    fun seekTo(positionMs: Long) = player.seekTo(positionMs)

    // === 收藏 ===
    fun toggleFavorite(songId: Long) {
        val current = _favoriteIds.value.toMutableSet()
        if (current.contains(songId)) {
            current.remove(songId)
        } else {
            current.add(songId)
        }
        _favoriteIds.value = current
    }

    fun isFavorite(songId: Long): Boolean = _favoriteIds.value.contains(songId)

    // === 重复/随机 ===
    fun toggleRepeatMode() {
        val current = playerState.value.repeatMode
        playerState.value.let { state ->
            val next = when (current) {
                RepeatMode.OFF -> RepeatMode.ALL
                RepeatMode.ALL -> RepeatMode.ONE
                RepeatMode.ONE -> RepeatMode.OFF
            }
            // 通过 ExoPlayer 原生 repeat
            player.getExoPlayer().repeatMode = when (next) {
                RepeatMode.OFF -> androidx.media3.common.Player.REPEAT_MODE_OFF
                RepeatMode.ALL -> androidx.media3.common.Player.REPEAT_MODE_ALL
                RepeatMode.ONE -> androidx.media3.common.Player.REPEAT_MODE_ONE
            }
        }
    }

    fun toggleShuffle() {
        val state = playerState.value
        player.getExoPlayer().shuffleModeEnabled = !state.shuffleMode
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
