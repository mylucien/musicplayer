package com.carmusic.player.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.carmusic.player.ui.theme.BgCard
import com.carmusic.player.ui.theme.TextPrimary
import com.carmusic.player.ui.theme.TextSecondary
import com.carmusic.player.ui.theme.ThemeBlue

data class TabItem(
    val id: String,
    val label: String
)

val topTabs = listOf(
    TabItem("home", "推荐"),
    TabItem("library", "音乐库"),
    TabItem("playlist", "歌单"),
    TabItem("charts", "排行榜"),
    TabItem("artists", "歌手"),
    TabItem("radio", "电台"),
    TabItem("settings", "设置")
)

@Composable
fun TopHeader(
    currentTab: String,
    onTabClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.Transparent)
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 顶部 Tab 导航
        Row(
            horizontalArrangement = Arrangement.spacedBy(28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            topTabs.forEach { tab ->
                val isActive = currentTab == tab.id
                Text(
                    text = tab.label,
                    color = if (isActive) TextPrimary else TextSecondary,
                    fontSize = 16.sp,
                    fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal,
                    modifier = Modifier.padding(bottom = if (isActive) 0.dp else 0.dp)
                )
                // active 指示器通过 Box 实现
            }
        }

        // 右侧操作区
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // 头像
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(BgCard),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=100&q=80",
                    contentDescription = "头像",
                    modifier = Modifier.size(32.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            // 时间
            Text(
                text = "10:30",
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
    }
}
