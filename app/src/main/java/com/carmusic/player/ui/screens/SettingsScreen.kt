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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carmusic.player.ui.theme.BgCard
import com.carmusic.player.ui.theme.BgMain
import com.carmusic.player.ui.theme.TextMuted
import com.carmusic.player.ui.theme.TextPrimary
import com.carmusic.player.ui.theme.TextSecondary
import com.carmusic.player.ui.theme.ThemeBlue

private data class SettingsNavItem(
    val icon: ImageVector,
    val label: String,
    val isActive: Boolean = false
)

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        // 左侧导航
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.width(200.dp)
        ) {
            listOf(
                SettingsNavItem(Icons.Default.Tune, "播放设置", isActive = true),
                SettingsNavItem(Icons.Default.Equalizer, "音效设置"),
                SettingsNavItem(Icons.Default.Tv, "显示设置"),
                SettingsNavItem(Icons.Default.Person, "账号与登录")
            ).forEach { item ->
                val bg = if (item.isActive) ThemeBlue else Color.Transparent
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(bg)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(item.icon, contentDescription = null,
                        tint = if (item.isActive) Color.White else TextSecondary,
                        modifier = Modifier.size(18.dp))
                    Text(item.label, color = if (item.isActive) Color.White else TextSecondary,
                        fontSize = 15.sp)
                }
            }
        }

        // 右侧内容
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x08FFFFFF))
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SettingsRow("在线播放音质", "高品质")
            SettingsRow("下载音质", "标准音质")
            SettingsRow("定时关闭", "关闭")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("自动播放", color = TextPrimary, fontSize = 15.sp)
                }
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ThemeBlue)
                        .padding(start = 22.dp)
                        .width(20.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                )
            }
        }
    }
}

@Composable
private fun SettingsRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Normal)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(value, color = TextSecondary, fontSize = 14.sp)
            Icon(Icons.Default.ChevronRight, contentDescription = null,
                tint = TextSecondary, modifier = Modifier.size(16.dp))
        }
    }
}
