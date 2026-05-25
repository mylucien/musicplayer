package com.carmusic.player.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.carmusic.player.ui.theme.BgSidebar
import com.carmusic.player.ui.theme.TextSecondary
import com.carmusic.player.ui.theme.ThemeBlue

data class SidebarItem(
    val id: String,
    val icon: ImageVector,
    val label: String
)

val sidebarItems = listOf(
    SidebarItem("home", Icons.Default.Home, "首页"),
    SidebarItem("library", Icons.Default.Album, "音乐库"),
    SidebarItem("favorites", Icons.Default.Favorite, "收藏"),
    SidebarItem("settings", Icons.Default.Settings, "设置")
)

@Composable
fun Sidebar(
    currentPage: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(72.dp)
            .fillMaxHeight()
            .background(BgSidebar)
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(ThemeBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = "Logo",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            sidebarItems.forEach { item ->
                val isActive = currentPage == item.id
                val bgColor = if (isActive) ThemeBlue else Color.Transparent
                val iconColor = if (isActive) Color.White else TextSecondary

                IconButton(
                    onClick = { onNavigate(item.id) },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(bgColor)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = iconColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
