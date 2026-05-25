package com.carmusic.player

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carmusic.player.ui.components.BottomPlaybackBar
import com.carmusic.player.ui.components.Sidebar
import com.carmusic.player.ui.components.TopHeader
import com.carmusic.player.ui.screens.ArtistsScreen
import com.carmusic.player.ui.screens.ChartsScreen
import com.carmusic.player.ui.screens.FavoritesScreen
import com.carmusic.player.ui.screens.FullscreenPlayerScreen
import com.carmusic.player.ui.screens.HomeScreen
import com.carmusic.player.ui.screens.LibraryScreen
import com.carmusic.player.ui.screens.PlaylistScreen
import com.carmusic.player.ui.screens.RadioScreen
import com.carmusic.player.ui.screens.SearchScreen
import com.carmusic.player.ui.screens.SettingsScreen
import com.carmusic.player.ui.theme.BgMain
import com.carmusic.player.ui.theme.CarMusicTheme
import com.carmusic.player.viewmodel.MusicViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarMusicTheme {
                CarMusicScreen()
            }
        }
    }
}

@Composable
fun CarMusicScreen(
    viewModel: MusicViewModel = viewModel()
) {
    val currentPage by viewModel.currentPage.collectAsState()
    val playerState by viewModel.playerState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    // 全屏播放页
    AnimatedVisibility(
        visible = currentPage == "fullplayer",
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        FullscreenPlayerScreen(
            isPlaying = playerState.isPlaying,
            onClose = { viewModel.navigateTo("home") },
            onTogglePlay = viewModel::togglePlay,
            onNext = viewModel::next,
            onPrevious = viewModel::previous
        )
    }

    // 主界面 (非全屏时)
    if (currentPage != "fullplayer") {
        Row(modifier = Modifier.fillMaxSize().background(BgMain)) {
            // 左侧导航栏
            Sidebar(
                currentPage = currentPage,
                onNavigate = { viewModel.navigateTo(it) }
            )

            // 右侧内容区
            Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // 顶部通栏
                    TopHeader(
                        currentTab = currentPage,
                        onTabClick = { viewModel.navigateTo(it) },
                        onSearchClick = { viewModel.navigateTo("search") }
                    )

                    // 内容舞台
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        when (currentPage) {
                            "home" -> HomeScreen(
                                onPlaylistClick = { viewModel.navigateTo("playlist") }
                            )
                            "library" -> LibraryScreen()
                            "playlist" -> PlaylistScreen()
                            "search" -> SearchScreen(
                                searchQuery = searchQuery,
                                searchResults = searchResults,
                                onQueryChange = viewModel::updateSearchQuery,
                                onSearch = viewModel::performSearch,
                                onPlaySong = viewModel::playSong
                            )
                            "settings" -> SettingsScreen()
                            "favorites" -> FavoritesScreen()
                            "charts" -> ChartsScreen()
                            "artists" -> ArtistsScreen()
                            "radio" -> RadioScreen()
                        }
                    }
                }

                // 底部播放控制栏 (覆盖在底部)
                BottomPlaybackBar(
                    currentSong = playerState.currentSong,
                    isPlaying = playerState.isPlaying,
                    onTogglePlay = viewModel::togglePlay,
                    onNext = viewModel::next,
                    onPrevious = viewModel::previous,
                    onOpenFullPlayer = { viewModel.navigateTo("fullplayer") },
                    modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter)
                )
            }
        }
    }
}
