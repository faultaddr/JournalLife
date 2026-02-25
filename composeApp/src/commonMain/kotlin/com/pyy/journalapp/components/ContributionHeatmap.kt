package com.pyy.journalapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.*

/**
 * 单日的贡献数据
 */
data class DayContribution(
    val date: LocalDate,
    val count: Int
) {
    val level: Int
        get() = when (count) {
            0 -> 0
            1, 2 -> 1
            3, 4 -> 2
            else -> 3
        }
}

/**
 * 一周的贡献数据
 */
data class WeekContribution(
    val weekStart: LocalDate,
    val days: List<DayContribution>
)

/**
 * GitHub 风格的贡献热图组件
 *
 * @param entries 日记条目日期列表
 * @param weeksToShow 显示的周数（默认16周，约4个月）
 * @param modifier 修饰符
 */
@Composable
fun ContributionHeatmap(
    entries: List<LocalDateTime>,
    weeksToShow: Int = 16,
    modifier: Modifier = Modifier
) {
    // 计算贡献数据
    val weeksData = remember(entries, weeksToShow) {
        calculateContributions(entries, weeksToShow)
    }

    // 统计信息
    val totalEntries = entries.size
    val activeDays = remember(entries) {
        entries.map { it.date }.distinct().size
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 标题行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📊",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "记录频率",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // 统计数字
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatText(
                        value = totalEntries.toString(),
                        label = "总篇数"
                    )
                    StatText(
                        value = activeDays.toString(),
                        label = "活跃天数"
                    )
                }
            }

            // 热图主体
            if (weeksData.isNotEmpty()) {
                HeatmapGrid(weeksData = weeksData)

                // 图例
                HeatmapLegend()
            } else {
                // 无数据状态
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "开始写日记，点亮你的第一格！",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StatText(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun HeatmapGrid(
    weeksData: List<WeekContribution>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        reverseLayout = true // 最近的时间在右侧
    ) {
        items(weeksData) { week ->
            WeekColumn(week = week)
        }
    }
}

@Composable
private fun WeekColumn(
    week: WeekContribution,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        week.days.forEach { day ->
            HeatmapCell(level = day.level)
        }
    }
}

@Composable
private fun HeatmapCell(
    level: Int,
    modifier: Modifier = Modifier
) {
    val color = when (level) {
        0 -> Color(0xFFE8E8E8) // 无记录 - 浅灰
        1 -> Color(0xFFB4E7E5) // 1-2篇 - 浅薄荷
        2 -> Color(0xFF85CDCA) // 3-4篇 - 中薄荷
        3 -> Color(0xFF4A9E9A) // 5+篇 - 深薄荷
        else -> Color(0xFFE8E8E8)
    }

    Box(
        modifier = modifier
            .size(12.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(color)
    )
}

@Composable
private fun HeatmapLegend(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "少",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(6.dp))

        // 图例格子
        (0..3).forEach { level ->
            HeatmapCell(level = level)
            Spacer(modifier = Modifier.width(3.dp))
        }

        Spacer(modifier = Modifier.width(2.dp))

        Text(
            text = "多",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 计算贡献数据 - 使用不可变模式
 */
private fun calculateContributions(
    entries: List<LocalDateTime>,
    weeksToShow: Int
): List<WeekContribution> {
    if (entries.isEmpty()) return emptyList()

    // 使用最新的日记日期作为基准
    val today = entries.maxByOrNull { it.date }?.date
        ?: return emptyList()

    // 按日期统计日记数量
    val dateCounts = entries
        .groupingBy { it.date }
        .eachCount()

    // 计算起始日期（从最近的周一开始）
    val daysFromMonday = today.dayOfWeek.ordinal // Monday = 0
    val weeksEnd = today.plus(DatePeriod(days = (6 - daysFromMonday)))
    val weeksStart = weeksEnd.minus(DatePeriod(days = (weeksToShow * 7 - 1)))

    // 使用不可变序列生成周数据
    return generateSequence(weeksStart) { weekStart ->
        weekStart.plus(DatePeriod(days = 7)).takeIf { it <= weeksEnd }
    }.map { weekStart ->
        val days = (0..6).map { dayOffset ->
            val date = weekStart.plus(DatePeriod(days = dayOffset))
            DayContribution(date = date, count = dateCounts[date] ?: 0)
        }
        WeekContribution(weekStart = weekStart, days = days)
    }.toList()
}

/**
 * 生成模拟的贡献数据（用于演示）
 */
fun generateMockContributions(): List<LocalDateTime> {
    val baseDate = LocalDate(2026, 2, 25)
    val mockEntries = mutableListOf<LocalDateTime>()

    // 生成最近60天的随机数据
    repeat(60) { dayOffset ->
        val date = baseDate.minus(DatePeriod(days = dayOffset))
        // 随机生成 0-5 篇日记
        val count = when ((date.dayOfMonth + date.monthNumber) % 5) {
            0 -> 0
            1, 2 -> 1
            3 -> 2
            else -> 3
        }

        repeat(count) {
            mockEntries.add(
                LocalDateTime(
                    year = date.year,
                    month = date.month,
                    dayOfMonth = date.dayOfMonth,
                    hour = (8..22).random(),
                    minute = (0..59).random()
                )
            )
        }
    }

    return mockEntries
}
