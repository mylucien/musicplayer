package com.carmusic.player.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carmusic.player.data.model.Song
import com.carmusic.player.ui.theme.BgCard
import com.carmusic.player.ui.theme.BgCardHover
import com.carmusic.player.ui.theme.TextPrimary
import com.carmusic.player.ui.theme.TextSecondary
import com.carmusic.player.ui.theme.ThemeBlue

@Composable
fun SearchScreen(
    searchQuery: String,
    searchResults: List<Song>,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onPlaySong: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // 搜索框
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                placeholder = {
                    Text("搜索歌曲、歌手、专辑", color = TextSecondary)
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = ThemeBlue,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = BgCard,
                    unfocusedContainerColor = BgCard
                )
            )

            // 语音按钮 (功能留空，纯 UI)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BgCard),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "语音",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 热门搜索
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("热门搜索:", color = TextSecondary, fontSize = 14.sp)
            listOf("周杰伦", "林俊杰", "陈奕迅", "五月天").forEach { tag ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(BgCard)
                        .clickable {
                            onQueryChange(tag)
                            onSearch()
                        }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(tag, color = TextSecondary, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 搜索结果
        if (searchResults.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(searchResults) { song ->
                    SearchResultItem(
                        song = song,
                        onClick = { onPlaySong(song) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    song: Song,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 封面
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BgCard),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }

        // 信息
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${song.artist} · ${song.album}",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        // 播放按钮
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(50))
                .background(BgCardHover),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search, // placeholder
                contentDescription = "播放",
                tint = TextSecondary,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}
