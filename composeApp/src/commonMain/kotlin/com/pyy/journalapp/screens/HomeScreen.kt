package com.pyy.journalapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pyy.journalapp.models.Book
import com.pyy.journalapp.models.JournalEntry
import com.pyy.journalapp.models.Visibility
import com.pyy.journalapp.ui.theme.JournalAppTheme
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    books: List<Book>,
    onBookClick: (Book) -> Unit,
    onAddBookClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // 计算统计数据
    val totalEntries = remember(books) { books.size * 3 } // 模拟数据
    val streakDays = remember(books) { 7 } // 模拟连续记录天数
    val timeCapsules = remember(books) { 2 } // 模拟时光胶囊数量

    JournalAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Journal Life",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    actions = {
                        TextButton(onClick = onSettingsClick) {
                            Text("⚙", fontSize = androidx.compose.ui.unit.TextUnit(20F, androidx.compose.ui.unit.TextUnitType.Sp))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = onAddBookClick
                ) {
                    Text("+ 新建书册")
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 欢迎区域
                item {
                    WelcomeHeader(
                        userName = "日记爱好者",
                        streakDays = streakDays
                    )
                }

                // 快速统计
                item {
                    QuickStatsRow(
                        totalBooks = books.size,
                        totalEntries = totalEntries,
                        streakDays = streakDays,
                        timeCapsules = timeCapsules
                    )
                }

                // 最近活动标题
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "我的书册",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        // 筛选标签
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = true,
                                onClick = { },
                                label = { Text("全部") }
                            )
                            FilterChip(
                                selected = false,
                                onClick = { },
                                label = { Text("公开") }
                            )
                        }
                    }
                }

                // 书册列表或空状态
                if (books.isEmpty()) {
                    item {
                        EmptyState(onAddBookClick = onAddBookClick)
                    }
                } else {
                    items(books) { book ->
                        BookCard(
                            book = book,
                            onClick = { onBookClick(book) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WelcomeHeader(
    userName: String,
    streakDays: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 问候语
        Text(
            text = getGreeting(),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        // 用户名和连续记录徽章
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = userName,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            // 连续记录徽章
            if (streakDays > 0) {
                StreakBadge(streakDays = streakDays)
            }
        }
    }
}

@Composable
private fun StreakBadge(streakDays: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFFF6B35).copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "🔥",
            fontSize = androidx.compose.ui.unit.TextUnit(16F, androidx.compose.ui.unit.TextUnitType.Sp)
        )
        Text(
            text = "已连续记录 $streakDays 天",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun QuickStatsRow(
    totalBooks: Int,
    totalEntries: Int,
    streakDays: Int,
    timeCapsules: Int,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        item {
            StatCard(
                icon = "📚",
                iconBackground = Color(0xFFE8A87C).copy(alpha = 0.2f),
                iconTint = Color(0xFFE8A87C),
                value = totalBooks.toString(),
                label = "书册"
            )
        }

        item {
            StatCard(
                icon = "✎",
                iconBackground = Color(0xFF85CDCA).copy(alpha = 0.2f),
                iconTint = Color(0xFF85CDCA),
                value = totalEntries.toString(),
                label = "日记"
            )
        }

        item {
            StatCard(
                icon = "🔥",
                iconBackground = Color(0xFFFF6B35).copy(alpha = 0.2f),
                iconTint = Color(0xFFFF6B35),
                value = streakDays.toString(),
                label = "连续天数"
            )
        }

        item {
            StatCard(
                icon = "⏳",
                iconBackground = Color(0xFFC38D9E).copy(alpha = 0.2f),
                iconTint = Color(0xFFC38D9E),
                value = timeCapsules.toString(),
                label = "时光胶囊"
            )
        }
    }
}

@Composable
private fun StatCard(
    icon: String,
    iconBackground: Color,
    iconTint: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 图标背景
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = androidx.compose.ui.unit.TextUnit(20F, androidx.compose.ui.unit.TextUnitType.Sp),
                    color = iconTint
                )
            }

            // 数值
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            // 标签
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun BookCard(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 为每个书册生成一个固定的渐变色
    val gradientColors = remember(book.id) {
        getGradientForBook(book.id)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = gradientColors[0].copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 渐变顶部区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = gradientColors,
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // 书册图标
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "📖",
                            fontSize = androidx.compose.ui.unit.TextUnit(24F, androidx.compose.ui.unit.TextUnitType.Sp)
                        )
                    }

                    // 隐私状态
                    val visibility = book.visibilityDefault
                    Surface(
                        color = if (visibility == Visibility.PUBLIC)
                            Color(0xFF4CAF50).copy(alpha = 0.9f)
                        else
                            Color(0xFF9E9E9E).copy(alpha = 0.9f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (visibility == Visibility.PUBLIC) "🌐" else "🔒",
                                fontSize = androidx.compose.ui.unit.TextUnit(12F, androidx.compose.ui.unit.TextUnitType.Sp)
                            )
                            Text(
                                text = if (visibility == Visibility.PUBLIC) "公开" else "私密",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // 书册信息
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1
                )

                val description = book.description
                if (description != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 底部信息
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${book.entriesCount} 篇日记",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = formatDate(book.updatedAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState(
    onAddBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 空状态图标
            Text(
                text = "📚",
                fontSize = androidx.compose.ui.unit.TextUnit(64F, androidx.compose.ui.unit.TextUnitType.Sp),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "还没有日记本",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = "创建你的第一个日记本，开始记录美好生活",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onAddBookClick,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+ 创建书册")
            }
        }
    }
}

// 辅助函数
private fun getGreeting(): String {
    return "你好" // 简化问候语
}

private fun getGradientForBook(bookId: String): List<Color> {
    val gradients = listOf(
        listOf(Color(0xFFF6D365), Color(0xFFFDA085)), // 温暖日落
        listOf(Color(0xFF4FACFE), Color(0xFF00F2FE)), // 海洋薄荷
        listOf(Color(0xFFFF9A9E), Color(0xFFFECFEF)), // 樱花粉
        listOf(Color(0xFF43E97B), Color(0xFF38F9D7)), // 森林绿
        listOf(Color(0xFF667EEA), Color(0xFF764BA2)), // 紫罗兰
        listOf(Color(0xFF8B4513), Color(0xFFD2691E))  // 温暖咖啡
    )

    // 根据bookId选择一个固定的渐变
    val index = bookId.hashCode().absoluteValue % gradients.size
    return gradients[index]
}

private fun formatDate(dateTime: LocalDateTime): String {
    val month = dateTime.monthNumber
    val day = dateTime.dayOfMonth
    val year = dateTime.year
    return "$year/$month/$day"
}

// 扩展属性，用于获取书册的日记数量
private val Book.entriesCount: Int
    get() = 3 // 模拟数据，实际应从数据库获取

private val kotlin.Int.absoluteValue: Int
    get() = if (this < 0) -this else this
