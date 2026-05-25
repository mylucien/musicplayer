package com.carmusic.player.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carmusic.player.ui.theme.BgCard
import com.carmusic.player.ui.theme.TextPrimary
import com.carmusic.player.ui.theme.TextSecondary

data class RecommendCard(
    val id: String,
    val icon: ImageVector,
    val title: String,
    val subtitle: String = "",
    val gradient: List<Color>
)

val recommendCards = listOf(
    RecommendCard("fav", Icons.Default.Favorite, "我喜欢的音乐", "128 首", listOf(Color(0xFF4e148c), Color(0xFF833ab4))),
    RecommendCard("daily", Icons.Default.FlashOn, "每日推荐", "", listOf(Color(0xFF1f4068), Color(0xFF162447))),
    RecommendCard("relax", Icons.Default.MusicNote, "放松时刻", "", listOf(Color(0xFF0f4c75), Color(0xFF3282b8))),
    RecommendCard("sport", Icons.Default.FlashOn, "运动节奏", "", listOf(Color(0xFFe85d04), Color(0xFFffb703))),
    RecommendCard("classic", Icons.Default.Radio, "怀旧经典", "", listOf(Color(0xFFd90429), Color(0xFFef233c)))
)

data class PlaylistSquare(
    val id: String,
    val icon: ImageVector,
    val iconColor: Color,
    val name: String
)

val playlistSquares = listOf(
    PlaylistSquare("hot", Icons.Default.LocalFireDepartment, Color(0xFFFF5722), "流行热歌榜"),
    PlaylistSquare("chinese", Icons.Default.MusicNote, Color(0xFFFFEB3B), "华语经典"),
    PlaylistSquare("chill", Icons.Default.MusicNote, Color(0xFF00BCD4), "轻音乐合集"),
    PlaylistSquare("west", Icons.Default.MusicNote, Color(0xFF9C27B0), "欧美潮流"),
    PlaylistSquare("night", Icons.Default.MusicNote, Color(0xFFE91E63), "深夜放空"),
    PlaylistSquare("ktv", Icons.Default.MusicNote, Color(0xFF4CAF50), "KTV必点曲目")
)

@Composable
fun HomeScreen(
    onPlaylistClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        // 为你推荐
        Text(
            text = "为你推荐",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            recommendCards.forEach { card ->
                val aspectRatio = if (card.id == "fav") 4f / 5f else 4f / 5f
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(aspectRatio)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.linearGradient(card.gradient))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = card.icon,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = card.title,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        if (card.subtitle.isNotBlank()) {
                            Text(
                                text = card.subtitle,
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // 推荐歌单
        Text(
            text = "推荐歌单",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            modifier = Modifier.padding(top = 28.dp, bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(playlistSquares) { item ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(BgCard)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF222222)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = item.iconColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = item.name,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
