package com.carmusic.player.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carmusic.player.ui.theme.BgCard
import com.carmusic.player.ui.theme.TextPrimary
import com.carmusic.player.ui.theme.TextSecondary

private data class ChartItem(
    val name: String,
    val tracks: List<String>,
    val badge: String,
    val badgeColor: Color
)

private val charts = listOf(
    ChartItem("热歌榜", listOf("1. 向云端 - 黄绮珊", "2. 起风了 - 买辣椒也用券", "3. 孤勇者 - 陈奕迅"), "HOT", Color(0xFFFF5722)),
    ChartItem("新歌榜", listOf("1. 笼 - 张碧晨", "2. 路过人间 - 郁可唯", "3. 篇章 - 张韶涵"), "NEW", Color(0xFF4CAF50)),
    ChartItem("飙升榜", listOf("1. 雪 Distance - Capper", "2. 就让这大雨全都落下 - 容祖儿", "3. 乌梅子酱 - 李荣浩"), "RISE", Color(0xFF2196F3))
)

@Composable
fun ChartsScreen(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        charts.forEach { chart ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BgCard)
                    .padding(18.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 14.dp)
                ) {
                    Text(
                        text = chart.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = chart.badge,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = chart.badgeColor
                    )
                }

                chart.tracks.forEach { track ->
                    Text(
                        text = track,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(vertical = 7.dp)
                    )
                }
            }
        }
    }
}
