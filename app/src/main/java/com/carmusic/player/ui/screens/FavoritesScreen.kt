package com.carmusic.player.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carmusic.player.ui.theme.BgCardHover
import com.carmusic.player.ui.theme.TextPrimary
import com.carmusic.player.ui.theme.TextSecondary

private data class FavTrack(
    val num: Int,
    val name: String,
    val artist: String,
    val time: String,
    val isFavorite: Boolean = true
)

private val demoFavorites = listOf(
    FavTrack(1, "晴天", "周杰伦", "04:29"),
    FavTrack(2, "平凡之路", "朴树", "05:02"),
    FavTrack(3, "蓝莲花", "许巍", "04:30"),
    FavTrack(4, "旅行的意义", "陈绮贞", "04:48")
)

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        item {
            Text(
                text = "我喜欢的音乐",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(demoFavorites) { track ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${track.num}",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.width(30.dp)
                )
                Text(
                    text = track.name,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = track.artist,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.width(100.dp)
                )
                Text(
                    text = track.time,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.width(50.dp)
                )
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "取消收藏",
                    tint = Color(0xFFE91E63),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
