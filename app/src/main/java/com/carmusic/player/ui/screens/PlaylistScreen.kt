package com.carmusic.player.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carmusic.player.ui.theme.BgCard
import com.carmusic.player.ui.theme.TextPrimary
import com.carmusic.player.ui.theme.TextSecondary
import com.carmusic.player.ui.theme.ThemeBlue

private data class Track(
    val num: String,
    val name: String,
    val artist: String,
    val time: String,
    val isActive: Boolean = false
)

private val demoPlaylist = listOf(
    Track("1", "平凡之路", "朴树", "05:02", isActive = true),
    Track("2", "蓝莲花", "许巍", "04:30"),
    Track("3", "旅行的意义", "陈绮贞", "04:48"),
    Track("4", "晴天", "周杰伦", "04:29")
)

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        // 左侧歌单信息
        Column(modifier = Modifier.width(260.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF6b705c), Color(0xFFa5a58d))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("旅行的意义", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("30 首歌曲 · 2.5 小时", color = TextSecondary, fontSize = 13.sp)
            Text(
                "旅行路上的音乐陪伴，记录每一段美好时光。",
                color = TextSecondary, fontSize = 13.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = ThemeBlue),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("播放全部")
                }

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = BgCard),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("收藏", color = TextPrimary)
                }
            }
        }

        // 右侧歌曲列表
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(demoPlaylist) { track ->
                val bg = if (track.isActive) ThemeBlue else Color.Transparent
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(bg)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(track.num, color = if (track.isActive) Color.White else TextSecondary,
                        fontSize = 14.sp, modifier = Modifier.width(30.dp))
                    Text(track.name, color = TextPrimary, fontWeight = FontWeight.Medium,
                        fontSize = 14.sp, modifier = Modifier.weight(1f))
                    Text(track.artist, color = if (track.isActive) Color.White.copy(0.8f) else TextSecondary,
                        fontSize = 13.sp, modifier = Modifier.width(120.dp))
                    Text(track.time, color = if (track.isActive) Color.White.copy(0.8f) else TextSecondary,
                        fontSize = 13.sp, modifier = Modifier.width(50.dp))
                }
            }
        }
    }
}
